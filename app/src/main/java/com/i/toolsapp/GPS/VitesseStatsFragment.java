package com.i.toolsapp.GPS;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.i.toolsapp.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class VitesseStatsFragment extends Fragment {

    ListView listView ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vitesse_stats, container, false);

        //ArrayList<SecondsPerVitesse> list = GetStats();
        ArrayList<SecondsPerVitesse> list = new ArrayList<>();
        list.add(new SecondsPerVitesse(get_seconds_between_interval(0,20),0,20));
        list.add(new SecondsPerVitesse(get_seconds_between_interval(20,35),20,35));
        list.add(new SecondsPerVitesse(get_seconds_between_interval(35,55),35,55));
        list.add(new SecondsPerVitesse(get_seconds_between_interval(55,75),55,75));
        list.add(new SecondsPerVitesse(get_seconds_between_interval(75,130),75,130));


        listView = v.findViewById(R.id.listview) ;
        VitesseStatsListAdapter adapter = new VitesseStatsListAdapter(getActivity(),list);
        listView.setAdapter(adapter) ;

        return v ;
    }

    /*public ArrayList<SecondsPerVitesse> GetStats(){
        ArrayList<SecondsPerVitesse> result = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (VitesseData vitesseData : StatsActivity.vitesseList){
            int index = StatsActivity.vitesseList.indexOf(vitesseData);
            if(!indexes.contains(index)){


            Double vitesse = vitesseData.getVitesse() ;

            int duree = 1 ;
            Double min_value = vitesse ;
            Double max_value = vitesse ;


            indexes.add(index);
            for (int i = index+1; i<StatsActivity.vitesseList.size();i++){
                if(!indexes.contains(i)){
                    if(StatsActivity.vitesseList.get(i).getVitesse()<=vitesseData.getVitesse()+2&&StatsActivity.vitesseList.get(i).getVitesse()>=vitesseData.getVitesse()-2){
                        duree++ ;
                        indexes.add(i);
                        if(max_value<StatsActivity.vitesseList.get(i).getVitesse()){
                            max_value = StatsActivity.vitesseList.get(i).getVitesse() ;
                        }else if(min_value>StatsActivity.vitesseList.get(i).getVitesse()){
                            min_value = StatsActivity.vitesseList.get(i).getVitesse() ;
                        }
                    }
                }
            }
            result.add(new SecondsPerVitesse(duree,min_value,max_value));
            }
        }
        return result ;
    }*/

    public int get_seconds_between_interval(int x,int y){
        int seconds = 0 ;
        for(VitesseData vitesse : StatsActivity.vitesseList){
            if(vitesse.getVitesse()>=x&&vitesse.getVitesse()<y){
                seconds++;
            }
        }
        return seconds ;
    }

}
