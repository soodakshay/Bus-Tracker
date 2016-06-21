package com.example.akshay.bus;

import com.google.gson.annotations.Expose;

/**
 * Created by Akshay on 9/15/2015.
 */
public class SnappedPoints {

    @Expose
    public Location location;

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
