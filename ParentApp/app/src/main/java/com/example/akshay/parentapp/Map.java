package com.example.akshay.parentapp;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Akshay on 8/31/2015.
 */
public class Map extends FragmentActivity {
    public final String URL = "http://akshay.site90.net";
    LatLng prev = null;
    LatLng current = null;
    int flag = 0;
    GoogleMap googleMap;
TimerTask timerTask;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.e("OnLocationChanged", "===========0");
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build();
                api api = restAdapter.create(api.class);
                api.getData(new Callback<getGSONData>() {
                    @Override
                    public void success(getGSONData getGSONData, Response response) {
                        Double LAT = getGSONData.getlat();
                        Double LONG = getGSONData.getlongitude();
                        current = new LatLng(LAT, LONG);

                        Log.e("===LAT", String.valueOf(LAT));
                        Log.e("===LONG", String.valueOf(LONG));
                        if (flag == 0 & LAT != null & LONG != null) {
                            prev = current;
                            flag = 1;
                        }
                        CameraUpdate cm = CameraUpdateFactory.newLatLngZoom(current, 18);

                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.RED).add(prev, current).width(20).visible(true);
                        googleMap.addPolyline(polylineOptions);
                        googleMap.animateCamera(cm);
                        prev = current;
                        current = null;


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("====", "Something gone wrong");
                    }
                });
            }
        };


        timer.schedule(timerTask,0,5000);








    }
}

