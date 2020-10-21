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


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.kushpel.trackensuretest.R;
import com.kushpel.trackensuretest.adapters.StatisticsAdapter;
import com.kushpel.trackensuretest.database.repository.StatisticsRepository;
import com.kushpel.trackensuretest.models.Statistics;
import java.util.List;


public class StatisticsFragment extends Fragment {

    private StatisticsRepository statisticsRepository;
    private ListView listView;
    private StatisticsAdapter<Statistics> statisticsList;
    private View view;
    public StatisticsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        statisticsRepository = new StatisticsRepository();
        statisticsList=new StatisticsAdapter<Statistics>(getActivity(),
                R.layout.statistics_item,statisticsRepository);
        observeToStatistics();
        listView = (ListView) view.findViewById(R.id.statisticsList);
        listView.setAdapter(statisticsList);
        return view;
    }

    private void observeToStatistics(){
        statisticsRepository.getAllTask().observe(getActivity(), new Observer<List<Statistics>>() {
            @Override
            public void onChanged(List<Statistics> StatisticsList) {
                if(statisticsList.size() > 0){
                    statisticsList.clear();
                }
                if(statisticsList != null){
                    statisticsList.addAll(StatisticsList);
                }
            }
        });
    }

}
