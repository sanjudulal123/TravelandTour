package com.death.tnt.home;

import com.google.firebase.database.DataSnapshot;

public class HomeInfoModule {
    private String place_name;
    private String place_cover_art_url;
    private String place_description;
    private String place_district;
    private String place_rating;

    public String getPlace_rating() {
        return place_rating;
    }

    public void setPlace_rating(String place_rating) {
        this.place_rating = place_rating;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_cover_art_url() {
        return place_cover_art_url;
    }

    public void setPlace_cover_art_url(String place_cover_art_url) {
        this.place_cover_art_url = place_cover_art_url;
    }

    public String getPlace_description() {
        return place_description;
    }

    public void setPlace_description(String place_description) {
        this.place_description = place_description;
    }

    public String getPlace_district() {
        return place_district;
    }

    public void setPlace_district(String place_district) {
        this.place_district = place_district;
    }
}
