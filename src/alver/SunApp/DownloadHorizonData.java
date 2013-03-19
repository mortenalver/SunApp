package alver.SunApp;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utility class for contacting the elevation server and downloading data for a given lat-lon coordinate.
 */
public class DownloadHorizonData {

    static final String serverAddress = "http://192.168.0.190/cgi-bin/script.py";

    private double lat;
    private double lon;
    private TextView tv;
    public String status = "";

    public DownloadHorizonData(double lat, double lon, TextView tv) {

        this.lat = lat;
        this.lon = lon;
        this.tv = tv;
    }

    public double[] getData() {
        try {
            URL url = new URL(serverAddress+"?latitude="+lat+"&longitude="+lon);

            Log.d(SunAppMain.LOGTAG, "URL: "+url);
            URLConnection urlConnection = url.openConnection();
            Log.d(SunAppMain.LOGTAG, "URL connection: "+urlConnection);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.d(SunAppMain.LOGTAG, "Inputstream: "+in);
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[1000];
            int count = 0;
            try {
                while ((count = in.read(buffer, 0, buffer.length)) >= 0)
                    sb.append(new String(buffer).substring(0, count));
            } finally {
                in.close();
            }

            return parseData(sb.toString());
        } catch (IOException ex) {
            Log.d(SunAppMain.LOGTAG, "Connection error", ex);
            return null;
        }
    }

    private double[] parseData(String data) {
        data = data.trim();
        //status = data.length()+":"+data;
        status = data.substring(0, 10)+":"+data.substring(data.length()-10);
        if ((data.charAt(0) != '[') || (data.charAt(data.length()-1) != ']'))
            return null;
        String[] parts = data.substring(1, data.length()-2).trim().split("\\s+");
        status = ""+parts.length+"("+parts[0]+")("+parts[parts.length-1]+")";
        double[] reply = new double[parts.length];
        for (int i=0; i<parts.length; i++) {
            try {
                reply[i] = Double.parseDouble(parts[i]);
            } catch (NumberFormatException ex) {
                reply[i] = -100;
            }
        }
        return reply;
    }
}
