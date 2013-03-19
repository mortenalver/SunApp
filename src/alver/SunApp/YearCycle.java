package alver.SunApp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import alver.SunApp.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alver
 * Date: 3/10/13
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class YearCycle extends Activity {

    private LocationManager locationManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.yearcycle);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        TextView out = (TextView)findViewById(R.id.tidText);
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        int dayOfYear = c.get(Calendar.DAY_OF_YEAR),
                hour = c.get(Calendar.HOUR_OF_DAY),
                minute = c.get(Calendar.MINUTE),
                second = c.get(Calendar.SECOND);
        double timeOfDay = hour/24. + minute/1440. + second/86400.;

        // Calculate day of year in radians:
        double gDegr = (360/365.25)*(dayOfYear + timeOfDay);
        double g = Math.PI*gDegr/180.;

        // Calculate solar declination:
        double dDegr = 0.396372-22.91327*Math.cos(g)+4.02543*Math.sin(g)-0.387205*Math.cos(2*g)+
                0.051967*Math.sin(2*g)-0.154527*Math.cos(3*g) + 0.084798*Math.sin(3*g);
        double D = Math.PI*dDegr/180.;

        // Calculate time correction:
        double TC = 0.004297+0.107029*Math.cos(g)-1.837877*Math.sin(g)-0.837378*Math.cos(2 * g)-
                2.340475*Math.sin(2 * g);

        // Get location:
        boolean gpsOn = displayGpsStatus();
        Location loc = gpsOn ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) :
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double SHAdegr = (hour-12)*15 + loc.getLongitude() + TC;
        if (SHAdegr < -180) SHAdegr += 360;
        else if (SHAdegr > 180) SHAdegr -= 360;
        double SHA = Math.PI*SHAdegr/180.;

        double cosSZA = Math.sin(loc.getLatitude())*Math.sin(D)+Math.cos(loc.getLatitude())*Math.cos(D)*Math.cos(SHA);
        double SZA = Math.acos(cosSZA);
        double SZAdegr = 180.*SZA/Math.PI;

        // Calculate azimuth angle:
        double cosAZ = (Math.sin(D)-Math.sin(loc.getLatitude())*Math.cos(SZA))/(Math.cos(loc.getLatitude())*Math.sin(SZA));
        double AZ = Math.acos(cosAZ);
        double AZdegr = 180.*AZ/Math.PI;

        out.setText(date.toString() + "\nDay of year: " + dayOfYear + "\ng = " + g
                + "\nDeclination: " + dDegr + "\nTime correction: " + TC + "\nSHAdegr = " + SHAdegr
                + "\nZenith angle: "+SZAdegr+"\nAZ: "+AZdegr);

    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }
}