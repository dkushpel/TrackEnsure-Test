package com.kushpel.trackensuretest.database.repository;

import androidx.lifecycle.LiveData;


import com.kushpel.trackensuretest.App;
import com.kushpel.trackensuretest.database.async.DeleteStatisticsAsyncTask;
import com.kushpel.trackensuretest.database.async.InsertStatisticsAsyncTask;
import com.kushpel.trackensuretest.database.async.UpdateStatisticsAsyncTask;
import com.kushpel.trackensuretest.database.dao.StatisticsDao;
import com.kushpel.trackensuretest.models.Statistics;

import java.util.List;

public class StatisticsRepository {
    private StatisticsDao statisticsDao;

    public StatisticsRepository() {
        statisticsDao = App.getInstance().getAppDatabase().StatisticsDao();
    }

    public void insertTask(Statistics Statistics){
        new InsertStatisticsAsyncTask(statisticsDao).execute(Statistics);
    }

    public void updateTask(Statistics Statistics){
        new UpdateStatisticsAsyncTask(statisticsDao).execute(Statistics);
    }

    public void deleteTask(Statistics Statistics){
        new DeleteStatisticsAsyncTask(statisticsDao).execute(Statistics);
    }

    public LiveData<List<Statistics>> getAllTask() {
        return statisticsDao.getAll();
    }

    public LiveData<List<Statistics>> getNotSyncedTask() {
        return statisticsDao.getNotSynced();
    }

    public LiveData<Statistics> getByNameTask(String StatisticsName) {
        return statisticsDao.getByName(StatisticsName);
    }

    public LiveData<Statistics> getByIdTask(long StatisticsId) {
        return statisticsDao.getById(StatisticsId);
    }
}
