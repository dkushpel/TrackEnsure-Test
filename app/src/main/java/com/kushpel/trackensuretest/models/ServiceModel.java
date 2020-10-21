package com.kushpel.trackensuretest.models;

import java.util.ArrayList;
import java.util.List;

public class ServiceModel {
    private static ServiceModel instance;
    private List<Station> stationToSync = new ArrayList<>();
    private List<Station> stationToRemove = new ArrayList<>();
    private List<Statistics> StatisticsToSync = new ArrayList<>();
    private boolean isConnected;

    private ServiceModel(){
    }

    public static ServiceModel getInstance(){
        if (instance == null){
            instance = new ServiceModel();
        }
        return instance;
    }

    public List<Station> getStationToSync() {
        return stationToSync;
    }

    public void setStationToSync(List<Station> stationToSync) {
        this.stationToSync = stationToSync;
    }

    public List<Station> getStationToRemove() {
        return stationToRemove;
    }

    public void setStationToRemove(List<Station> stationToRemove) {
        this.stationToRemove = stationToRemove;
    }

    public List<Statistics> getStatisticsToSync() {
        return StatisticsToSync;
    }

    public void setStatisticsToSync(List<Statistics> StatisticsToSync) {
        this.StatisticsToSync = StatisticsToSync;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
