package com.example.akshay.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay on 9/15/2015.
 */
public class Location {

    @SerializedName("latitude")
    @Expose
    Double latitude;
    @SerializedName("longitude")
    @Expose
    Double longitude;


    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }



}
