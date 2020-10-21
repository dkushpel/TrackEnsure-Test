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
package com.kushpel.trackensuretest.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kushpel.trackensuretest.R;
import com.kushpel.trackensuretest.database.repository.StatisticsRepository;
import com.kushpel.trackensuretest.models.Statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class StatisticsAdapter<T> extends ArrayAdapter<Statistics> {

    private static final String TAG = StatisticsAdapter.class.getName();
    List<Statistics> stationList=new ArrayList();
    private LayoutInflater layoutInflater;
    private Activity activity;
    StatisticsRepository statisticsRepository;


    public StatisticsAdapter(Activity activity, int resource, StatisticsRepository statisticsRepository ) {
        super(activity, resource );

        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.statisticsRepository=statisticsRepository;

    }

    @Override
    public int getCount() {
        return stationList.size();
    }
    @Override
    public void add(@Nullable Statistics object) {
        stationList.add(object);
        super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Statistics> collection) {
        stationList.addAll(collection);
        super.addAll(collection);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parentView) {

        // Inflate the layout
        if (convertView == null) {

            // Get the layout
            convertView = layoutInflater.inflate(R.layout.statistics_item, parentView, false);
            if (position % 2 == 0) {
                convertView.setBackgroundResource(R.color.gray_darker);
            } else {
                convertView.setBackgroundResource(R.color.gray);
            }
            // Get the parts
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView type = (TextView) convertView.findViewById(R.id.address);

            // Set the parts
            Statistics statistics = stationList.get(position);
            name.setText(statistics.getName());
            type.setText(statistics.getAddress());


        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        if (getCount() == 0) {
            return 1;
        }

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int size(){
        return stationList.size();
    }


    @Override
    public void clear() {
        stationList.clear();
        super.clear();
    }


}
