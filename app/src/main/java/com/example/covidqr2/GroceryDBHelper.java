package com.example.covidqr2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.covidqr2.MyCovidEntry.*;
public class GroceryDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "covid_personal.db";
    public static final int DATABASE_VERSION = 4;
    public GroceryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_COVID_TABLE = "CREATE TABLE " +
                MyCovidEntry.CovidEntry.TABLE_NAME + " (" +
                MyCovidEntry.CovidEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyCovidEntry.CovidEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                //MyCovidEntry.CovidEntry.COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                MyCovidEntry.CovidEntry.COLUMN_MYTIME + " TEXT NOT NULL, "+
                CovidEntry.COLUMN_CHECKED + " TEXT NOT NULL, "+
                MyCovidEntry.CovidEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_COVID_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyCovidEntry.CovidEntry.TABLE_NAME);
        onCreate(db);
    }
}
