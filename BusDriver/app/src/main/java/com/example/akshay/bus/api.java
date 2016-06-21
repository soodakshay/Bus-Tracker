package com.example.akshay.bus;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Akshay on 9/15/2015.
 */
public interface api {

    @GET("/snapToRoads?&interpolate=true&key=AIzaSyDAsAs9k2dQ6aDMUlgLcE-dTxAOxMtADTU")
    public void getData(@Query("path") String latlng, Callback<SnappedPointsContainer> response);

}
