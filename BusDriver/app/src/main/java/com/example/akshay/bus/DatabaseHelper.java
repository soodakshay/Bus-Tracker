package com.example.akshay.bus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Akshay on 10/13/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    final static String DATABASE_NAME = "student.db";
    final static String TABLE_NAME = "details";
    final static String COL_1 = "srno";
    final static String COL_2 = "sname";
    final static String COL_3 = "class";
    final static String COL_4 = "latitude";
    final static String COL_5 = "longitude";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY " +
                ", " + COL_2 + " TEXT(25), " + COL_3 + " TEXT(10) , " + COL_4 + " INTEGER(15) ," + COL_5 + " INTEGER(15)";
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
