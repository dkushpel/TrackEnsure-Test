package com.kushpel.trackensuretest.database.async;

import android.os.AsyncTask;

import com.kushpel.trackensuretest.database.dao.StationDao;
import com.kushpel.trackensuretest.models.Station;

public class UpdateStationAsyncTask extends AsyncTask<Station, Void, Void> {

    private StationDao StationDao;

    public UpdateStationAsyncTask(StationDao StationDao) {
        this.StationDao = StationDao;
    }

    @Override
    protected Void doInBackground(Station... stations){
        this.StationDao.update(stations);
        return null;
    }
}
