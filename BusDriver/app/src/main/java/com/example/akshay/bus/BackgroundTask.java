package com.example.akshay.bus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by Akshay on 9/1/2015.
 */
public class BackgroundTask extends AsyncTask<Void, Void, Void> {
    String URL;
    Context context;
    ProgressDialog progressDialog;
    URI url;
    int ROUTE;
    int SHIFT;

    public BackgroundTask(String url, Context c , int route , int SHIFT) {
        this.URL = url;
        this.context = c;
        this.ROUTE = route;
        this.SHIFT = SHIFT;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Starting Service..");
        progressDialog.show();

    }


    @Override
    protected Void doInBackground(Void... params) {

        HttpClient httpClient = new DefaultHttpClient();
        try {
            url = new URI(URL+"?route="+ URLEncoder.encode(String.valueOf(ROUTE)) + "&shift=" + URLEncoder.encode(String.valueOf(SHIFT) , "utf-8"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpGet get = new HttpGet();
        get.setURI(url);
        try {
            httpClient.execute(get);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

    }
}


