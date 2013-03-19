package alver.SunApp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SunAppMain extends Activity implements View.OnClickListener {
    private LocationListener locationListener;
    public LocationManager locationManager;
    private int count = 0;

    public static final String LOGTAG = "SolMain";



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        //TextView tv = new TextView(this);
        //tv.setText("Welcome to Android world! by mirage");
        //setContentView(tv);
        Button b = (Button)findViewById(R.id.posBut);
        b.setOnClickListener(this);

        b = (Button)findViewById(R.id.tidBut);
        b.setOnClickListener(new SolvinkelListener(this));

        b = (Button)findViewById(R.id.getHorizon);
        b.setOnClickListener(new GetHorizonData((TextView)findViewById(R.id.horizonText), this));

        //ImageView iv = (ImageView)findViewById(R.id.imageView);
        //iv.setImageResource(R.drawable.astg1);//Bitmap(BitmapFactory.decodeFile("/res/map/ASTGTM2_N66E014_num.tif"));

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

    }


    @Override
    public void onClick(View v) {

        //Intent myIntent = new Intent(this, LocationChooser.class);

        //startActivity(myIntent);
        /*
        TextView tv = (TextView)findViewById(R.id.textView);
        boolean gpsOn = displayGpsStatus();

        locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 10,locationListener);

        Location loc = gpsOn ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) :
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (loc != null) {
            String longitude = "Longitude: " +loc.getLongitude();
            String latitude = "Latitude: " +loc.getLatitude();

            String s = longitude+"\n"+latitude;
            tv.setText("GPS "+(gpsOn ? "on" : "off")+", "+longitude+" : "+latitude);
        }
        else tv.setText("GPS "+(gpsOn ? "on" : "off")+", no location available");
        */
    }

    public Location getLocation() {
        boolean gpsOn = displayGpsStatus();

        return gpsOn ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) :
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            TextView editLocation = (TextView)findViewById(R.id.textView);

            editLocation.setText("");
            //pb.setVisibility(View.INVISIBLE);
            Toast.makeText(getBaseContext(), "Location changed : Lat: " +
                    loc.getLatitude() + " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " +loc.getLongitude();
            //Log.v(TAG, longitude);
            String latitude = "Latitude: " +loc.getLatitude();
            //Log.v(TAG, latitude);


            String s = longitude+"\n"+latitude;
            editLocation.setText((count++)+": "+s);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

}
