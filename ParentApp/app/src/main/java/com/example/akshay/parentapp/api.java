package com.example.akshay.parentapp;

import retrofit.http.GET;

/**
 * Created by Akshay on 9/8/2015.
 */
public interface api {
    @GET("/getLats.php?username=rakesh")
    public void getData( retrofit.Callback<getGSONData> response);
}
