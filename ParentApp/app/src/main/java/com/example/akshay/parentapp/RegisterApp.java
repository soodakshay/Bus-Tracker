package com.example.akshay.parentapp;

/**
 * Created by Akshay on 8/19/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;


public class RegisterApp extends AsyncTask<Void, Void, String> {

    private static final String TAG = "GCMRelated";
    Context ctx;
    GoogleCloudMessaging gcm;
    String SENDER_ID = "237184799993";
    String regid = null;
    private int appVersion;
    String SNAME, CLASS, FNAME, MNAME;
    int ROLLNO, ROUTE, SHIFT;
    Long MNO, FNO;
    Double LAT, LONG;
    Activity activity;

    public RegisterApp(Context ctx, int rollno, String sname, GoogleCloudMessaging gcm, String classs, String fname, Long fno, String mname, Long mno, int route, int SHIFT, Double lat, Double longg, Activity activity, int appVersion) {
        this.ctx = ctx;
        this.ROLLNO = rollno;
        this.SNAME = sname;
        this.CLASS = classs;
        this.FNAME = fname;
        this.FNO = fno;
        this.MNAME = mname;
        this.MNO = mno;
        this.ROUTE = route;
        this.SHIFT = SHIFT;
        this.LAT = lat;
        this.LONG = longg;
        this.activity = activity;
        this.gcm = gcm;
        this.appVersion = appVersion;

        Log.e("In Constructor", "-======================");

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("In PreExecute", "-======================");
    }


    @Override
    protected String doInBackground(Void... arg0) {
        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            regid = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regid;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            try {
                sendRegistrationIdToBackend();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.e("URI EXCEPTION", "WTF!!!");
            }

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(ctx, regid);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        Log.e("MESSAGE ", msg);
        Log.e("ROLLNO", String.valueOf(ROLLNO));
        return msg;

    }

    private void storeRegistrationId(Context ctx, String regid) {
        final SharedPreferences prefs = ctx.getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("registration_id", regid);
        editor.putInt("appVersion", appVersion);
        editor.commit();
        Log.e("In Shared Prefs", "-======================");

    }


    private void sendRegistrationIdToBackend() throws URISyntaxException, UnsupportedEncodingException {
        Log.e("INSEND REG TO BACK", "HEY");
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        String MYURL = "http://akshay.site90.net/register.php?rollno=" + URLEncoder.encode(String.valueOf(ROLLNO)) + "&sname=" + URLEncoder.encode(SNAME, "utf-8") + "&regid=" + regid + "&class=" + URLEncoder.encode(CLASS) + "&fname=" + URLEncoder.encode(FNAME) + "&fno=" + URLEncoder.encode(String.valueOf(FNO)) + "&mname=" + URLEncoder.encode(MNAME) + "&mno=" + URLEncoder.encode(String.valueOf(MNO)) + "&route=" + ROUTE + "&shift=" + URLEncoder.encode(String.valueOf(SHIFT)) + "&latitude=" + URLEncoder.encode(String.valueOf(LAT)) + "&longitude=" + URLEncoder.encode(String.valueOf(LONG));
        request.setURI(new URI(MYURL));
        Log.e("RESULT", "http://akshay.site90.net/register.php?rollno=" + ROLLNO + "&sname=" + SNAME + "&regid=" + regid + "&class=" + CLASS + "&fname=" + FNAME + "&fno=" + FNO + "&mname=" + MNAME + "&mno=" + MNO + "&route=" + ROUTE + "&shift=" + SHIFT + "&latitude=" + LAT + "&longitude=" + LONG);

        try {

            httpclient.execute(request);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("Something Went Wrong ", "ClientPro");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("Something Went Wrong ", "IOEX");
        }
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(ctx, "Registration Completed. Now you can see the notifications", Toast.LENGTH_SHORT).show();
        Log.v(TAG, result);

        Intent i = new Intent(ctx, Map.class);
        activity.startActivity(i);
        activity.finish();

    }
}