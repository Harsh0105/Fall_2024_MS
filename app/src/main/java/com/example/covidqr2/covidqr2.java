package com.example.covidqr2;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class covidqr2 extends Application {



        @Override
        public void onCreate() {
            super.onCreate();


            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }


