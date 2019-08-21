package com.death.tnt.favourite;

public class FavouriteModule {
    private String place;
    private double lati;
    private double longi;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public FavouriteModule(String place, Double lati, Double longi) {
        this.place = place;
        this.lati = lati;
        this.longi = longi;
    }



}
