package com.kushpel.trackensuretest.database.async;

import android.os.AsyncTask;

import com.kushpel.trackensuretest.database.dao.StatisticsDao;
import com.kushpel.trackensuretest.models.Statistics;


public class InsertStatisticsAsyncTask extends AsyncTask<Statistics, Void, Void> {

    private StatisticsDao statisticsDao;

    public InsertStatisticsAsyncTask(StatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
    }

    @Override
    protected Void doInBackground(Statistics... statistics) {
        this.statisticsDao.insert(statistics);
        return null;
    }
}
