package com.kushpel.trackensuretest.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.kushpel.trackensuretest.database.dao.StationDao;
import com.kushpel.trackensuretest.database.dao.StatisticsDao;
import com.kushpel.trackensuretest.models.Statistics;
import com.kushpel.trackensuretest.models.Station;

@Database(entities = {Station.class, Statistics.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StatisticsDao StatisticsDao();
    public abstract StationDao stationDao();


}
