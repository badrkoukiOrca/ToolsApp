package com.i.toolsapp.GPS;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.i.toolsapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class StatsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        GraphView graph = (GraphView) findViewById(R.id.graph);

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
        avgSpeed.append("\n"+SpeedHistory.AverageSpeed);

    }

}
