package com.kushpel.trackensuretest.database.async;

import android.os.AsyncTask;

import com.kushpel.trackensuretest.database.dao.StatisticsDao;
import com.kushpel.trackensuretest.models.Statistics;


public class DeleteStatisticsAsyncTask extends AsyncTask<Statistics, Void, Void> {

    private StatisticsDao statisticsDao;

    public DeleteStatisticsAsyncTask(StatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
    }

    @Override
    protected Void doInBackground(Statistics... statistics) {
        this.statisticsDao.delete(statistics);
        return null;
    }
}
