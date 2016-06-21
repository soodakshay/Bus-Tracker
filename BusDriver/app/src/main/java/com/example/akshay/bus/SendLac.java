package com.example.akshay.bus;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Akshay on 9/7/2015.
 */
public class SendLac extends AsyncTask<Void , Void , Void> {
    String URL;
    Double LAT , LONG;
    Context context;

    public SendLac(String url , Double lat , Double longg , Context context )

    {
        this.URL = url;
        this.LAT = lat;
        this.LONG = longg;
        this.context= context;

    }





    @Override
    protected Void doInBackground(Void... params) {
        URI uri = null;
        try {
            uri = new URI(URL + "?username=rakesh&lat=" + LAT + "&longitude=" + LONG );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("Something Gone Wrong" , "WTF");
        }
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet();
        get.setURI(uri);
       Log.e("====", "Doing Job");
        Log.e("======", URL + "?username=rakesh&lat=" + LAT + "&longitude=" + LONG);
        try {
            client.execute(get);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
