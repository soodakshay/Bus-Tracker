package com.example.akshay.parentapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Akshay on 9/2/2015.
 */
public class Main extends Activity {
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pg = new ProgressDialog(this);
        pg.setTitle("Please Wait!!");
        pg.setMessage("Checking Registration ID");
        pg.show();
        final SharedPreferences myprefs = getSharedPreferences(Main.class.getSimpleName(), MODE_PRIVATE);
        String regID = myprefs.getString("registration_id", "");
        String AppVer = myprefs.getString("appVersion", "");
        if (regID.isEmpty()) {
            pg.dismiss();
            startActivity(new Intent(Main.this, RegisterApp.class));
        } else {
            pg.dismiss();

            startActivity(new Intent(Main.this, Map.class));

        }
    }
}
