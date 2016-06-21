package com.example.akshay.parentapp;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay on 9/8/2015.
 */
public class getGSONData {

    @SerializedName("lat")
    @Expose
    public String lat;

    @SerializedName("longitude")
    @Expose
    public String longitude;

    public Double getlat() {
        return Double.parseDouble(lat);
    }

    public void setLat(String lat) {

        this.lat = lat;
    }

    public Double getlongitude() {

        return Double.parseDouble(longitude);
    }

    public void setLong(String longitude) {

        this.longitude = longitude;
    }

}
