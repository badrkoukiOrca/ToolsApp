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

public class VitesseListAdapter extends ArrayAdapter<VitesseData> {

    private final Activity context;
    private final ArrayList<VitesseData> vitesses;

    public VitesseListAdapter(Activity context, ArrayList<VitesseData> vitesseData) {
        super(context, R.layout.vitesse_per_seconds_cell, vitesseData);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.vitesses=vitesseData;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.vitesse_per_seconds_cell, null,true);

        TextView vitesseText = (TextView) rowView.findViewById(R.id.vitesse_value);
        TextView hourText = (TextView) rowView.findViewById(R.id.hour_value);
        ImageView vitesse_status = rowView.findViewById(R.id.vitesse_status) ;


        vitesseText.setText(vitesses.get(position).getVitesse().toString()+"Km/h");
        hourText.setText(vitesses.get(position).getHour());

        if(position>0){
            if(vitesses.get(position).getVitesse()>vitesses.get(position-1).getVitesse()){
                vitesse_status.setImageResource(R.drawable.up_arrow);
            }else if(vitesses.get(position).getVitesse()<vitesses.get(position-1).getVitesse()){
                vitesse_status.setImageResource(R.drawable.dow_arrow);
            }else{
                vitesse_status.setImageResource(R.drawable.constant_arrow);
            }
        }

        return rowView;

    };
}
