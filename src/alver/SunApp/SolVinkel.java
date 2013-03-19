package alver.SunApp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: alver
 * Date: 3/14/13
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolVinkel {

    public static double[][] getDailyCycle(int cycleLength, double latitude, double longitude, Date time) {

        double[][] res = new double[2][cycleLength];

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(time);
        int dayOfYear = c.get(Calendar.DAY_OF_YEAR);

        for (int i=0; i<cycleLength; i++) {
            double hour = i*24./cycleLength;
            double timeOfDay = hour;///24. + minute/1440. + second/86400.;

            // Calculate day of year in radians, not taking time-of-day into account (to be able to provide a continuous curve):
            double gDegr = (360/365.25)*(dayOfYear + 0*timeOfDay);
            double g = Math.PI*gDegr/180.;

            // Calculate solar declination:
            double dDegr = 0.396372-22.91327*Math.cos(g)+4.02543*Math.sin(g)-0.387205*Math.cos(2*g)+
                    0.051967*Math.sin(2*g)-0.154527*Math.cos(3*g) + 0.084798*Math.sin(3*g);
            double D = Math.PI*dDegr/180.;

            // Calculate time correction:
            double TC = 0.004297+0.107029*Math.cos(g)-1.837877*Math.sin(g)-0.837378*Math.cos(2 * g)-
                    2.340475*Math.sin(2 * g);


            double SHAdegr = (hour-12)*15 /*+ longitude*/ + TC;
            if (SHAdegr < -180) SHAdegr += 360;
            else if (SHAdegr > 180) SHAdegr -= 360;
            double SHA = Math.PI*SHAdegr/180.;

            double cosSZA = Math.sin(latitude)*Math.sin(D)+Math.cos(latitude)*Math.cos(D)*Math.cos(SHA);
            if (cosSZA > 1) cosSZA = 1;
            if (cosSZA < -1) cosSZA = -1;
            double SZA = Math.acos(cosSZA);
            double SZAdegr = 180.*SZA/Math.PI;

            // Calculate azimuth angle:
            double cosAZ = (Math.sin(D)-Math.sin(latitude)*Math.cos(SZA))/(Math.cos(latitude)*Math.sin(SZA));
            double AZ = Math.acos(cosAZ);
            double AZdegr = 180.*AZ/Math.PI;

            res[0][i] = 180+SHAdegr;
            res[1][i] = 90-SZAdegr;
        }

        return res;
    }
}
