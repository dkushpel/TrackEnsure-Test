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
package com.kushpel.trackensuretest.fragments;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kushpel.trackensuretest.R;
import com.kushpel.trackensuretest.activities.RedactorMapActivity;
import com.kushpel.trackensuretest.adapters.StationAdapter;
import com.kushpel.trackensuretest.database.repository.StationRepository;
import com.kushpel.trackensuretest.models.Station;

import java.util.List;


public class StationsFragment extends Fragment {

    private static final String TAG = StationsFragment.class.getName();
    private StationRepository StationRepository;
    private View libraryView;
    private ListView listView;
    private StationAdapter<Station> stationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        libraryView = inflater.inflate(R.layout.fragment_stations, container, false);
        StationRepository = new StationRepository();
        stationList=new StationAdapter<Station>(getActivity(),
                R.layout.station_item,StationRepository);
        observeToStations();
        FloatingActionButton addStationButton = (FloatingActionButton) libraryView.findViewById(R.id.addStationButton);
        addStationButton.setOnClickListener(addStationButtonClick);
        listView = (ListView) libraryView.findViewById(R.id.stationlist);
        listView.setAdapter(stationList);
        return libraryView;
    }


    private void observeToStations(){
        StationRepository.getAllTask().observe(getViewLifecycleOwner(), new Observer<List<Station>>() {
            @Override
            public void onChanged(List<Station> StationList) {

                if(stationList.size() > 0){
                    stationList.clear();
                }
                if(StationList != null){
                    stationList.addAll(StationList);

                }
            }
        });
    }

    private View.OnClickListener addStationButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), RedactorMapActivity.class);
            startActivity(intent);
        }
    };


}
