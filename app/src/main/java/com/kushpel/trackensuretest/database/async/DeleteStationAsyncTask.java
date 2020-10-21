package com.kushpel.trackensuretest.database.async;

import android.os.AsyncTask;

import com.kushpel.trackensuretest.database.dao.StationDao;
import com.kushpel.trackensuretest.models.Station;


public class DeleteStationAsyncTask extends AsyncTask<Station, Void, Void> {

    private StationDao StationDao;

    public DeleteStationAsyncTask(StationDao StationDao) {
        this.StationDao = StationDao;
    }

    @Override
    protected Void doInBackground(Station... stations) {
        this.StationDao.delete(stations);
        return null;
    }
}
