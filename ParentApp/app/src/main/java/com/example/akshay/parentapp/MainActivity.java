package com.example.akshay.parentapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends Activity implements View.OnClickListener, OnItemSelectedListener {
    //GCM
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCMRelated";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;
    Spinner spinner , SHIFT;
    int SHIFTT;
Double LAT , LONG;

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText RollNo, SName, Class, FatherName, FatherNo, MotherName, MotherNo;
    String ROLLNO, SNAME, ROUTE, FATHERNAME, FATHERNO, MOTHERNAME, MOTHERNO, TIMINGS, CLASS, REGID;
    // url to create new student
    private static String url_create_product = "http://www.funvilla.in/school/create_product.php";
    private static final String TAG_SUCCESS = "success";

Activity activity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        activity = this;
        spinner = (Spinner) findViewById(R.id.SP_Route);
        List<String> list = new ArrayList<String>();
        list.add("Jagadhari");
        list.add("Yamunanagar");
        list.add("Radaur");
        list.add("Workshop");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        SHIFT = (Spinner) findViewById(R.id.SPINNER_SHIFT);
        List<String> shift = new ArrayList<String>();
        shift.add("Shift 1");
        shift.add("Shift 2");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_spinner_item,shift);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        SHIFT.setAdapter(adapter1);
        SHIFT.setOnItemSelectedListener(this);



        //GCM
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            regid = getRegistrationId(getApplicationContext());
            if (!regid.isEmpty()) {
                finish();
                startActivity(new Intent(MainActivity.this, Map.class));
               /* pg.dismiss();*/

            }

        }

        RollNo = (EditText) findViewById(R.id.ET_ROLL);
        SName = (EditText) findViewById(R.id.ET_SNAME);
        Class = (EditText) findViewById(R.id.ET_CLASS);
        FatherName = (EditText) findViewById(R.id.ET_FatherName);
        FatherNo = (EditText) findViewById(R.id.ET_FATHERNO);
        MotherName = (EditText) findViewById(R.id.ET_MotherName);
        MotherNo = (EditText) findViewById(R.id.ET_MotherNo);
        /*RouteNo = (EditText) findViewById(R.id.ET_Route);*/



        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.BT_SaveStudentRecord);
        Button setLocation = (Button) findViewById(R.id.BT_GETLOCATION);
        setLocation.setOnClickListener(this);

        // button click event
        btnCreateProduct.setOnClickListener(this);


    }

    /**
     * @return Application's {@code SharedPreferences}.
     */

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(getApplicationContext());
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LAT = data.getDoubleExtra("LAT" , 0);
        LONG = data.getDoubleExtra("LONG" , 0);

    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.BT_GETLOCATION)
        {
            Intent intent = new Intent(MainActivity.this , GetLocationMap.class);
            startActivityForResult(intent , 0);
        }



if(v.getId() == R.id.BT_SaveStudentRecord) {
    ROLLNO = RollNo.getText().toString();
    SNAME = SName.getText().toString();
    CLASS = Class.getText().toString();
    FATHERNAME = FatherName.getText().toString();
    FATHERNO = FatherNo.getText().toString();
    MOTHERNAME = MotherName.getText().toString();
    MOTHERNO = MotherNo.getText().toString();
       /* ROUTE = RouteNo.getText().toString();*/
    if (checkPlayServices()) {
        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        regid = getRegistrationId(getApplicationContext());
        Log.e("ROLLNO==========", ROLLNO);
        Log.e("SNAME==========", SNAME);
        Log.e("REGID==========", regid);
        Log.e("CLASS==========", CLASS);
        Log.e("FATHERNAME==========", FATHERNAME);
        Log.e("FATHERNO==========", FATHERNO);
        Log.e("MOTHERNAME==========", MOTHERNAME);
        Log.e("MOTHERNO==========", MOTHERNO);
        Log.e("ROUTE==========", ROUTE);
        Log.e("SHIFT==========", String.valueOf(SHIFTT));
        Log.e("LAT==========", String.valueOf(LAT));
        Log.e("LONG==========", String.valueOf(LONG));
        if (regid.isEmpty()) {

            new RegisterApp(getApplicationContext(), Integer.parseInt(ROLLNO), SNAME, gcm, CLASS, FATHERNAME, Long.parseLong(FATHERNO), MOTHERNAME, Long.parseLong(MOTHERNO), Integer.parseInt(ROUTE), SHIFTT, LAT, LONG, activity, getAppVersion(getApplicationContext())).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Device already Registered", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Map.class));
        }
    } else {
        Log.i(TAG, "No valid Google Play Services APK found.");
    }
}
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
       if((spinner.getId() == R.id.SPINNER_SHIFT))
        {
            switch (parent.getItemAtPosition(position).toString()) {
                case "Shift 1":
                  SHIFTT = 1;
                    break;
                case "Shift 2":
                   SHIFTT = 2;
                    break;


            }
        }
        else if(spinner.getId() == R.id.SP_Route)
        {
            switch (parent.getItemAtPosition(position).toString()) {
                case "Jagadhari":
                    ROUTE = "401";
                    break;
                case "Yamunanagar":
                    ROUTE = "402";
                    break;
                case "Radaur":
                    ROUTE = "403";
                    break;
                case "Workshop":
                    ROUTE = "404";
                    break;

            }
        }




        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * Background Async Task to Create new product
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        /**
         * Gets the current registration ID for application on GCM service.
         * <p/>
         * If result is empty, the app needs to register.
         *
         * @return registration ID, or empty string if there is no existing
         * registration ID.
         */
        private String getRegistrationId(Context context) {
            final SharedPreferences prefs = getGCMPreferences(context);
            String registrationId = prefs.getString(PROPERTY_REG_ID, "");
            if (registrationId.isEmpty()) {
                Log.i(TAG, "Registration not found.");
                return "";
            }
            // Check if app was updated; if so, it must clear the registration ID
            // since the existing regID is not guaranteed to work with the new
            // app version.
            int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
            int currentVersion = getAppVersion(getApplicationContext());
            if (registeredVersion != currentVersion) {
                Log.i(TAG, "App version changed.");
                return "";
            }
            return registrationId;
        }


        /**
         * Creating product
         */
        protected String doInBackground(String... args) {


            String LAT = "78.555454";
            String LONG = "78.545545";
            REGID = "jefk3je3eh";
            Log.e("ROLLNO==========", ROLLNO);
            Log.e("SNAME==========", SNAME);
            Log.e("REGID==========", regid);
            Log.e("CLASS==========", CLASS);
            Log.e("FATHERNAME==========", FATHERNAME);
            Log.e("FATHERNO==========", FATHERNO);
            Log.e("MOTHERNAME==========", MOTHERNAME);
            Log.e("MOTHERNO==========", MOTHERNO);
            Log.e("ROUTE==========", ROUTE);
            Log.e("Timings==========", TIMINGS);
            Log.e("LAT==========", LAT);
            Log.e("LONG==========", LONG);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("rollno", ROLLNO));
            params.add(new BasicNameValuePair("sname", SNAME));
            params.add(new BasicNameValuePair("regid", regid));
            params.add(new BasicNameValuePair("class", CLASS));
            params.add(new BasicNameValuePair("fname", FATHERNAME));
            params.add(new BasicNameValuePair("fno", FATHERNO));
            params.add(new BasicNameValuePair("mname", MOTHERNAME));
            params.add(new BasicNameValuePair("mno", MOTHERNO));
            params.add(new BasicNameValuePair("route", ROUTE));
            params.add(new BasicNameValuePair("timings", TIMINGS));
            params.add(new BasicNameValuePair("latitude", LAT));
            params.add(new BasicNameValuePair("longitude", LONG));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.e("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Map.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }


}


