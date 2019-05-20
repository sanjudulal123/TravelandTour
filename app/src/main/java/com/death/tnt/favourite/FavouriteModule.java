package com.death.tnt.favourite;

public class FavouriteModule {
    private String place;
    private Double lati;
    private Double longi;

    public FavouriteModule(String place, Double lati, Double longi) {
        this.place = place;
        this.lati = lati;
        this.longi = longi;
    }

    public Double getLati() {
        return lati;
    }

    public Double getLongi() {
        return longi;
    }



    public String getPlace() {
        return place;
    }

}
