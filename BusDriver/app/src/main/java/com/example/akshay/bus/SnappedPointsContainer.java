package com.example.akshay.bus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Akshay on 9/15/2015.
 */
public class SnappedPointsContainer {

    @SerializedName("snappedPoints")
    @Expose
    List<SnappedPoints> snappedPointsList;

    public void setSnappedPointsList(List<SnappedPoints> snappedPointsList) {
        this.snappedPointsList = snappedPointsList;
    }

    public List<SnappedPoints> getSnappedPointsList() {
        return snappedPointsList;
    }
}
