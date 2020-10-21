/*
Kushp Music Player
Copyright (C) 2019 David Zhang

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kushpel.trackensuretest.activities;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kushpel.trackensuretest.adapters.SlidePagerAdapter;
import com.kushpel.trackensuretest.database.repository.StatisticsRepository;
import com.kushpel.trackensuretest.database.repository.StationRepository;
import com.kushpel.trackensuretest.models.Statistics;
import com.kushpel.trackensuretest.models.Station;
import com.kushpel.trackensuretest.services.DBService;
import com.kushpel.trackensuretest.utils.Constants;
import com.kushpel.trackensuretest.utils.NetworkStateReceiver;
import com.kushpel.trackensuretest.widgets.FontTabLayout;
import com.kushpel.trackensuretest.R;

import java.util.ArrayList;
import java.util.List;


public class  MainActivity extends BaseActivity  {

    private static final String TAG = MainActivity.class.getName();
    private NetworkStateReceiver receiver;
    private FirebaseAuth mAuth;
    private StationRepository stationRepository;
    private StatisticsRepository statisticsRepository;
    private ViewPager viewPager;
    FirebaseUser user;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainView();
        stationRepository = new StationRepository();
        statisticsRepository = new StatisticsRepository();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            user = mAuth.getCurrentUser();

                            observeToChangedData();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        receiver = new NetworkStateReceiver();
        registerConnectionReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void registerConnectionReceiver(BroadcastReceiver receiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initMainView() {

        // Init views
        viewPager = (ViewPager) findViewById(R.id.headers);
        PagerAdapter pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        // Give tab layout
        FontTabLayout tabLayout = (FontTabLayout) findViewById(R.id.slider);
        tabLayout.setupWithViewPager(viewPager);

        // Set title
        MainActivity.this.setTitle("Track Ensure App");
    }

    private Observer<List<Station>> StationsObserver = new Observer<List<Station>>() {
        @Override
        public void onChanged(List<Station> stationList) {
            if(!stationList.isEmpty()){
                Intent intent = new Intent(MainActivity.this, DBService.class);
                intent.putParcelableArrayListExtra(Constants.STATION_KEY, (ArrayList<Station>) stationList);
                startService(intent);
            }
        }
    };

    private Observer<List<Statistics>> StatisticsObserver = new Observer<List<Statistics>>() {
        @Override
        public void onChanged(List<Statistics> statisticsList) {
            if(!statisticsList.isEmpty()){
                Intent intent = new Intent(MainActivity.this, DBService.class);
                intent.putParcelableArrayListExtra(Constants.GAS_STATIONS_KEY, (ArrayList<Statistics>) statisticsList);
                startService(intent);
            }
        }
    };

    private Observer<List<Station>> deletedStationsObserver = new Observer<List<Station>>() {
        @Override
        public void onChanged(List<Station> stationList) {
            if(!stationList.isEmpty()){
                Intent intent = new Intent(MainActivity.this, DBService.class);
                intent.putParcelableArrayListExtra(Constants.DELETED_STATIONS_KEY, (ArrayList<Station>) stationList);
                startService(intent);
            }
        }
    };
    private void observeToChangedData(){
        stationRepository.getNotSyncedTask().observe(this, StationsObserver);
        statisticsRepository.getNotSyncedTask().observe(this, StatisticsObserver);
        stationRepository.getDeletedTask().observe(this, deletedStationsObserver);
    }

    public void updateContent() {
        Log.d(TAG, "Updating content.");
        viewPager.getAdapter().notifyDataSetChanged();
    }
}
