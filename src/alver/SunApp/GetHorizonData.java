package alver.SunApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class GetHorizonData implements OnClickListener {
    Button btn;
    private TextView textView;
    private SunAppMain solMain;
    double[] reply = null;
    double[][] cycle = null;
    ProgressDialog progressDialog = null;

    public GetHorizonData(TextView textView, SunAppMain solMain) {
        this.textView = textView;
        this.solMain = solMain;

    }


    public void onClick(View view){

        new LongOperation().execute("");

    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(SunAppMain.LOGTAG, "Getting location...");
            Location loc = solMain.getLocation();
            Log.d(SunAppMain.LOGTAG, "Location: "+loc);
            double lat, lon;
            if (loc != null) {
                lat = loc.getLatitude();
                lon = loc.getLongitude();
            } else {
                // Sunndalsøra:
                //lat = 62.6752;
                //lon = 8.5633;
                // Fagernes:
                lat = 60.9858;
                lon = 9.2324;
            }
            Log.d(SunAppMain.LOGTAG, "Callong horizon download routine");
            DownloadHorizonData dhd = new DownloadHorizonData(lat, lon, textView);
            reply = dhd.getData();
            Log.d(SunAppMain.LOGTAG, ""+dhd.status);
            // Calculate sun angle for a daily cycle for the current location and date:
            if (reply != null) {
                cycle = SolVinkel.getDailyCycle(reply.length, lat, lon,
                        GregorianCalendar.getInstance().getTime());
                Log.d(SunAppMain.LOGTAG, cycle.length+", "+cycle[0].length);
            }
            else cycle = null;
            return "Executed";
        }


        @Override
        protected void onPostExecute(String result) {
            Log.d(SunAppMain.LOGTAG, "Done downloading data");
            if (reply != null) {
                Intent myIntent = new Intent(textView.getContext(), HorizonChart.class);
                myIntent.putExtra("horizonData", reply);
                if (cycle != null) {
                    myIntent.putExtra("cycleHorizontal", cycle[0]);
                    myIntent.putExtra("cycleAngle", cycle[1]);
                }
                textView.getContext().startActivity(myIntent);
            } else {
                Context context = solMain.getApplicationContext();
                Toast toast = Toast.makeText(context, "Unable to download data", Toast.LENGTH_LONG);
                toast.show();
            }

        }

        @Override
        protected void onPreExecute() {
            Context context = solMain.getApplicationContext();
            CharSequence text = "Contacting geography server...";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //solMain.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            //solMain.setProgressBarIndeterminateVisibility(true);

            /*Log.d(SolMain.LOGTAG, "Opening progress dialog");
            progressDialog = ProgressDialog.show(solMain.this, "Please wait", "Contacting geography server...", true);
            progressDialog.setCancelable(false);
            Log.d(SolMain.LOGTAG, "Done"); */

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}