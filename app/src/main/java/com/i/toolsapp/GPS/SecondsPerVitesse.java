package com.i.toolsapp.GPS;

public class SecondsPerVitesse {

    private int duree ;
    private double vitesse_min ;
    private double vitesse_max ;


    public SecondsPerVitesse(int duree, double vitesse_min, double vitesse_max) {
        this.duree = duree;
        this.vitesse_min = vitesse_min;
        this.vitesse_max = vitesse_max;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public double getVitesse_min() {
        return vitesse_min;
    }

    public void setVitesse_min(double vitesse_min) {
        this.vitesse_min = vitesse_min;
    }

    public double getVitesse_max() {
        return vitesse_max;
    }

    public void setVitesse_max(double vitesse_max) {
        this.vitesse_max = vitesse_max;
    }
}
