package com.i.toolsapp.GPS;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.i.toolsapp.R;

import java.util.ArrayList;

public class VitesseStatsListAdapter extends ArrayAdapter<SecondsPerVitesse> {

    private final Activity context;
    private final ArrayList<SecondsPerVitesse> vitesses;

    public VitesseStatsListAdapter(Activity context, ArrayList<SecondsPerVitesse> vitesseData) {
        super(context, R.layout.vitesse_stats_cell, vitesseData);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.vitesses=vitesseData;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.vitesse_stats_cell, null,true);

        TextView vitesseText = (TextView) rowView.findViewById(R.id.vitesse_value);
        TextView hourText = (TextView) rowView.findViewById(R.id.hour_value);


        vitesseText.setText(vitesses.get(position).getDuree()+"sec");

        if(vitesses.get(position).getVitesse_min()==vitesses.get(position).getVitesse_max()){
            hourText.setText(vitesses.get(position).getVitesse_min()+"Km/h");
        }else{
            hourText.setText(vitesses.get(position).getVitesse_min()+"Km/h"+" - "+vitesses.get(position).getVitesse_max()+"Km/h");
        }




        return rowView;

    };
}
