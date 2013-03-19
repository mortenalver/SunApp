package alver.SunApp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: alver
 * Date: 3/10/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolvinkelListener implements View.OnClickListener {

    private Activity parent;

    public SolvinkelListener(Activity parent) {

        this.parent = parent;
    }
    @Override
    public void onClick(View v) {

        Intent myIntent = new Intent(v.getContext(), YearCycle.class);
        v.getContext().startActivity(myIntent);

    }
}
