package com.example.akshay.bus;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button start;
    public static final String NOTIFICATION_URL = "http://www.akshay.site90.net/sendnote.php";
    public static final String UPDATE_LAT_LONG = "http://www.akshay.site90.net/sendlats.php";
    public static final String ROADS_API_URL = "https://roads.googleapis.com/v1";
    public static final String SERVER_URL = "http://akshay.site90.net";
    Context context = MainActivity.this;
    Spinner ROUTEE, SHIFT;
    int ROUTE;
    Double LAT = null, LONG = null;
    int flag = 0;
    LatLng prev = null;

    int i = 0;
    boolean flaaag = false;
    LatLng mylatlng;
    int SHIFTT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDetailsFromServer();
        start = (Button) findViewById(R.id.button_START);
        start.setOnClickListener(this);
        ROUTEE = (Spinner) findViewById(R.id.SPINNER_ROUTE);
        SHIFT = (Spinner) findViewById(R.id.SPINNER_SHIFT);
        List<String> shift = new ArrayList<String>();
        shift.add("Shift 1");
        shift.add("Shift 2");
        List<String> route = new ArrayList<String>();
        route.add("Jagadhari");
        route.add("Yamunanagar");
        route.add("Radaur");
        route.add("Workshop");
        ArrayAdapter<String> adROUTE = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, route);
        adROUTE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adSHIFT = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, shift);
        adSHIFT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ROUTEE.setAdapter(adROUTE);
        SHIFT.setAdapter(adSHIFT);
        ROUTEE.setOnItemSelectedListener(MainActivity.this);

    }

    private void getDetailsFromServer() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(SERVER_URL).setLogLevel(RestAdapter.LogLevel.FULL).build();

    }

    @Override
    public void onClick(View v) {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LAT = location.getLatitude();
                LONG = location.getLongitude();
                Log.e("====LOCATION UPDATE====", "LAT==" + String.valueOf(LAT) + "LONG==" + String.valueOf(LONG));
                String latlng = String.valueOf(LAT) + "," + String.valueOf(LONG);

                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(ROADS_API_URL).setLogLevel(RestAdapter.LogLevel.FULL).build();
                api api = restAdapter.create(api.class);
                api.getData(latlng, new Callback<SnappedPointsContainer>() {
                    @Override
                    public void success(SnappedPointsContainer snappedPointsContainer, Response response) {
                        LAT = snappedPointsContainer.getSnappedPointsList().get(0).getLocation().getLatitude();
                        LONG = snappedPointsContainer.getSnappedPointsList().get(0).getLocation().getLongitude();
                        mylatlng = new LatLng(LAT, LONG);
                        if (flag == 0 & LAT != null & LONG != null) {
                            prev = mylatlng;
                            flag = 1;
                        }

                        GoogleMap googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mylatlng, 18);
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.add(prev, mylatlng).color(Color.BLUE).width(15).visible(true);
                        googleMap.addPolyline(polylineOptions);
                        googleMap.animateCamera(update);

                        prev = mylatlng;
                        mylatlng = null;
                        new SendLac(UPDATE_LAT_LONG, LAT, LONG, MainActivity.this).execute();
                        if (flaaag == false) {
                            new BackgroundTask(NOTIFICATION_URL, context, ROUTE , SHIFTT).execute();
                            flaaag = true;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Failure Log", "Something gone wrong");
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
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.SPINNER_ROUTE) {
            switch (parent.getItemAtPosition(position).toString()) {
                case "Jagadhari":
                    ROUTE = 401;
                    Toast.makeText(MainActivity.this, String.valueOf(ROUTE), Toast.LENGTH_SHORT).show();
                    break;
                case "Yamunanagar":
                    ROUTE = 402;
                    Toast.makeText(MainActivity.this, String.valueOf(ROUTE), Toast.LENGTH_SHORT).show();
                    break;
                case "Radaur":
                    ROUTE = 403;
                    Toast.makeText(MainActivity.this, String.valueOf(ROUTE), Toast.LENGTH_SHORT).show();
                    break;
                case "Workshop":
                    ROUTE = 404;
                    Toast.makeText(MainActivity.this, String.valueOf(ROUTE), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (spinner.getId() == R.id.SPINNER_SHIFT) {
            switch (parent.getItemAtPosition(position).toString()) {
                case "Shift 1":
                    SHIFTT = 1;
                    break;
                case "Shift 2":
                    SHIFTT = 2;
                    break;
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
