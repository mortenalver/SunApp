package alver.SunApp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.*;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint.Align;

import org.achartengine.*;
import org.achartengine.chart.*;
import org.achartengine.model.*;
import org.achartengine.renderer.*;

public class HorizonChart extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        double[] data = i.getDoubleArrayExtra("horizonData");
        double[] cycleHorizontal = i.getDoubleArrayExtra("cycleHorizontal");
        double[] cycleAngle = i.getDoubleArrayExtra("cycleAngle");
        LinearLayout ll  = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        final XYChart lc = new LineChart(getDataset(data, cycleHorizontal, cycleAngle), getRenderer());
        final GraphicalView graph = new GraphicalView(getApplicationContext(),lc);
        ll.addView(graph);
        setContentView(ll);
    }

    private XYMultipleSeriesRenderer getRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setXLabelsColor(Color.YELLOW);
        renderer.setShowGrid(true);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setChartTitle("Horizon profile");
        renderer.setXTitle("Horizontal angle");
        renderer.setYTitle("Vertical angle");
        renderer.addXTextLabel(0, "North");
        renderer.addXTextLabel(90, "West");
        renderer.addXTextLabel(180, "South");
        renderer.addXTextLabel(270, "East");
        XYSeriesRenderer r = new XYSeriesRenderer();
        //r.setLineWidth(10);
        r.setStroke(BasicStroke.SOLID);
        r.setColor(Color.WHITE);
        r.setFillBelowLineColor(Color.WHITE);
        r.setFillBelowLine(true);
        //r.setPointStyle(PointStyle.CIRCLE);
        renderer.addSeriesRenderer(r);
        r = new XYSeriesRenderer();
        r.setFillBelowLine(true);
        r.setStroke(BasicStroke.SOLID);
        r.setColor(Color.RED);
        renderer.addSeriesRenderer(r);
        renderer.setZoomButtonsVisible(false);
        return renderer;
    }

    private XYMultipleSeriesDataset getDataset(double[] data, double[] cycleHorizontal, double[] cycleAngle) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries series = new XYSeries("Horizon");
        XYSeries sun = new XYSeries("Sun angle");
        for (int i = 0; i < data.length; i++) {
            series.add(i*360./(double)data.length,data[i]);
            if (cycleHorizontal != null)
                sun.add(cycleHorizontal[i], Math.max(0, cycleAngle[i]));
        }

        dataset.addSeries(series);

        if (cycleHorizontal != null) {
            dataset.addSeries(sun);
        }
        return dataset;
    }

}