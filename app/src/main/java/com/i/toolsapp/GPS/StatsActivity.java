package com.i.toolsapp.GPS;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.i.toolsapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class StatsActivity extends AppCompatActivity implements View.OnClickListener {


    public static ArrayList<VitesseData> vitesseList ;
    Button btn1 ;
    Button btn2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Historique du vitesse.");

        cartesian.yAxis(0).title("Vitesse");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        for(VitesseData vitesse : StatsActivity.vitesseList){
            seriesData.add(vitesse);
        }


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value' }");


        Line series3 = cartesian.line(series3Mapping);
        series3.name("Bateau");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);


        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);


        //-------------
        Fragment fragment = new TableVitessePerSeconds();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        btn1.setBackgroundColor(Color.parseColor("#898A8B"));


       /*GraphView graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series ;

        DataPoint[] dataPoints = new DataPoint[SpeedHistory.speedHistory.size()];

        for(int i=0;i<SpeedHistory.speedHistory.size();i++){
            dataPoints[i] = new DataPoint(i,SpeedHistory.speedHistory.get(i) );
        }
        series = new LineGraphSeries<DataPoint>(dataPoints);
        graph.addSeries(series);


        TextView maxSpeed = findViewById(R.id.maxspeed);
        TextView avgSpeed = findViewById(R.id.avgspeed);

        maxSpeed.append("\n"+SpeedHistory.MaxSpeed);
        avgSpeed.append("\n"+SpeedHistory.AverageSpeed);*/

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn1){
            btn1.setBackgroundColor(Color.parseColor("#898A8B"));
            btn2.setBackgroundColor(Color.parseColor("#1E88E5"));
            Fragment fragment = new TableVitessePerSeconds();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }else{
            btn2.setBackgroundColor(Color.parseColor("#898A8B"));
            btn1.setBackgroundColor(Color.parseColor("#1E88E5"));
            Fragment fragment = new VitesseStatsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);

        }

    }

}
