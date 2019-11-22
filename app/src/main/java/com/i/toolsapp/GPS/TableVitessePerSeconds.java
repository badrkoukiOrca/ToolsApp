package com.i.toolsapp.GPS;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.i.toolsapp.R;


public class TableVitessePerSeconds extends Fragment {

    ListView listView ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.fragment_table_vitesse_per_seconds, container, false);

        listView = v.findViewById(R.id.listview) ;
        VitesseListAdapter adapter = new VitesseListAdapter(getActivity(),StatsActivity.vitesseList);
        listView.setAdapter(adapter) ;



        return v;
    }
}
