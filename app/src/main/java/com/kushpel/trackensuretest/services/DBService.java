package com.kushpel.trackensuretest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kushpel.trackensuretest.database.repository.StatisticsRepository;
import com.kushpel.trackensuretest.database.repository.StationRepository;
import com.kushpel.trackensuretest.models.Statistics;
import com.kushpel.trackensuretest.models.ServiceModel;
import com.kushpel.trackensuretest.models.Station;
import com.kushpel.trackensuretest.utils.Constants;

import java.util.List;

public class DBService extends IntentService {
    private static final String TAG = DBService.class.getName();
    private StatisticsRepository statisticsRepository;
    private StationRepository stationRepository;
    private DatabaseReference databaseReference;
    private static final String Statistics_for_Firebase = "Statistics";
    private static final String Station_for_Firebase = "Stations";
    private ServiceModel model;

    public DBService() {
        super(TAG);
        statisticsRepository = new StatisticsRepository();
        stationRepository = new StationRepository();
        model = ServiceModel.getInstance();
    }

    private boolean isAuth() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (!getUserId().isEmpty()){
            databaseReference = database.getReference(getUserId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras.containsKey(Constants.IS_NETWORK_CONNECTED_KEY)){
            boolean isConnected = extras.getBoolean(Constants.IS_NETWORK_CONNECTED_KEY);
            model.setConnected(isConnected);
        }
        if (extras.containsKey(Constants.STATION_KEY)) {
            List<Station> stationToSync = extras.getParcelableArrayList(Constants.STATION_KEY);
            model.setStationToSync(stationToSync);

        }
        if (extras.containsKey(Constants.GAS_STATIONS_KEY)) {
            List<Statistics> StatisticsToSync = extras.getParcelableArrayList(Constants.GAS_STATIONS_KEY);
            model.setStatisticsToSync(StatisticsToSync);
        }
        if (extras.containsKey(Constants.DELETED_STATIONS_KEY)) {
            List<Station> StationsToRemove = extras.getParcelableArrayList(Constants.DELETED_STATIONS_KEY);
            model.setStationToRemove(StationsToRemove);
        }
        if (isAuth() && model.isConnected()){
            syncStations(model.getStationToSync());
            syncStatistics(model.getStatisticsToSync());
            removeStations(model.getStationToRemove());
        }
    }

    private void removeStations(List<Station> StationList){
        for (int i = 0; i < StationList.size(); i++) {
            removeFromFirebase(Station_for_Firebase, StationList.get(i));
        }
    }

    private void syncStations(List<Station> Stations){
        for (int i = 0; i < Stations.size(); i++) {
            final Station Station = Stations.get(i);
            databaseReference.child(Station_for_Firebase).child(String.valueOf(Station.getId())).setValue(Station)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Station.setSynced(true);
                            model.getStationToSync().remove(Station);
                            stationRepository.updateTask(Station);
                        }
                    });
        }
    }



    private String getUserId() {
        String userId = "";
        try {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception ignored){ }
        return userId;
    }
    private void syncStatistics(List<Statistics> Statisticss){
        for (int i = 0; i < Statisticss.size(); i++) {
            final Statistics Statistics = Statisticss.get(i);
            databaseReference.child(Statistics_for_Firebase).child(String.valueOf(Statistics.getId())).setValue(Statistics)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Statistics.setSynced(true);
                            model.getStatisticsToSync().remove(Statistics);
                            statisticsRepository.updateTask(Statistics);
                        }
                    });
        }
    }
    private void removeFromFirebase(String child, final Station Station){
        Query applesQuery = databaseReference.child(child).orderByChild("id").equalTo(Station.getId());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    model.getStationToRemove().remove(Station);
                    stationRepository.deleteTask(Station);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
