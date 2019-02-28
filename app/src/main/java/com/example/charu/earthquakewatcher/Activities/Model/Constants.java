package com.example.charu.earthquakewatcher.Activities.Model;

import java.util.Random;

public class Constants {

    public static final String URL="https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.geojson";
    public static final int LIMIT=40;

    public static int RandomInt(int max,int min)
    {
        return new Random().nextInt(max-min)+min;
    }

}
