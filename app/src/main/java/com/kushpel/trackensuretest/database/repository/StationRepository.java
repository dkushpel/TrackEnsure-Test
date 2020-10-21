package com.kushpel.trackensuretest.database.repository;

import androidx.lifecycle.LiveData;

import com.kushpel.trackensuretest.App;
import com.kushpel.trackensuretest.database.async.DeleteStationAsyncTask;
import com.kushpel.trackensuretest.database.async.InsertStationAsyncTask;
import com.kushpel.trackensuretest.database.async.UpdateStationAsyncTask;
import com.kushpel.trackensuretest.database.dao.StationDao;
import com.kushpel.trackensuretest.models.Station;

import java.util.List;

public class StationRepository {
    private StationDao stationDao;

    public StationRepository() { stationDao = App.getInstance().getAppDatabase().stationDao(); }

    public void insertTask(Station Station){ new InsertStationAsyncTask(stationDao).execute(Station); }

    public void updateTask(Station Station){ new UpdateStationAsyncTask(stationDao).execute(Station); }

    public void deleteTask(Station Station){ new DeleteStationAsyncTask(stationDao).execute(Station); }

    public LiveData<List<Station>> getAllTask() {
        return stationDao.getAll();
    }

    public LiveData<List<Station>> getNotSyncedTask() {
        return stationDao.getNotSynced();
    }

    public LiveData<List<Station>> getDeletedTask() {
        return stationDao.getDeleted();
    }
}
