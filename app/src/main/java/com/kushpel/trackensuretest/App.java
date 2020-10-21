package com.kushpel.trackensuretest;

import android.app.Application;
import androidx.room.Room;

import com.kushpel.trackensuretest.database.AppDatabase;


public class App extends Application {

    public static App instance;
    private AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
    }
    public static App getInstance() {
        return instance;
    }
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}