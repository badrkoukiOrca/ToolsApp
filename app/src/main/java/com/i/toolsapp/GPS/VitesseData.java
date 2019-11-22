package com.i.toolsapp.GPS;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class VitesseData extends ValueDataEntry {

    private String hour ;
    private Double vitesse ;

    public VitesseData(String hour, Double vitesse) {
        super(hour,vitesse);
        this.hour = hour;
        this.vitesse = vitesse;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Double getVitesse() {
        return vitesse;
    }

    public void setVitesse(Double vitesse) {
        this.vitesse = vitesse;
    }
}
