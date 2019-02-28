package com.example.charu.earthquakewatcher.Activities.Model;

public class Earthquake {
   private String place;
   private double magnitude;
   private long time;
   private String detailLink;
   private String type;
    private double lat;
   private double longit;

    public Earthquake() {
        this.place = place;
        this.magnitude = magnitude;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
        this.lat = lat;
        this.longit = longit;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongit() {
        return longit;
    }

    public void setLongit(double longit) {
        this.longit = longit;
    }
}
