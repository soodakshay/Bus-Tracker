package com.example.akshay.parentapp;

import android.app.Fragment;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Akshay on 10/12/2015.
 */
public class GetLocationMap extends FragmentActivity {
    GoogleMap googleMap;
    LocationManager locationManager;
    String BESTPROVIDER;
    Double LAT = null;
    Double LONG = null;
    Button SETLOC;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getlocationmap);
        SETLOC = (Button) findViewById(R.id.BT_FINALIZELOC);
        SETLOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LAT == null && LONG == null)
                {
                    Toast.makeText(GetLocationMap.this, "Please wait while GPS is searching for your location" , Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra("LAT" , LAT);
                    intent.putExtra("LONG" , LONG);
                    setResult(0 , intent);
                    finish();
                }
            }
        });
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.getLocationMap)).getMap();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        BESTPROVIDER = locationManager.getBestProvider(criteria, true);
        locationManager.requestSingleUpdate(BESTPROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LAT = location.getLatitude();
                LONG = location.getLongitude();
                MarkerOptions markerOptions = new MarkerOptions();
                googleMap.addMarker(markerOptions.draggable(true).visible(true).position(new LatLng(LAT, LONG)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(LAT , LONG) , 15);

                googleMap.animateCamera(cameraUpdate);
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        LatLng latLngFromMarkerPosition = marker.getPosition();
                        LAT = latLngFromMarkerPosition.latitude;
                        LONG = latLngFromMarkerPosition.longitude;
                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, null);


    }
}
