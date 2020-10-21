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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.kushpel.trackensuretest.R;
import com.kushpel.trackensuretest.activities.MainActivity;
import com.kushpel.trackensuretest.activities.RedactorMapActivity;
import com.kushpel.trackensuretest.database.repository.StationRepository;
import com.kushpel.trackensuretest.models.Station;
import com.kushpel.trackensuretest.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class StationAdapter<T> extends ArrayAdapter<Station> {

    private static final String TAG = StationAdapter.class.getName();
    List<Station> stationList=new ArrayList();
    private LayoutInflater layoutInflater;
    private Activity activity;
    StationRepository stationRepository;


    public StationAdapter(Activity activity, int resource,StationRepository stationRepository) {
        super(activity, resource );
        //this.stationList = songList;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.stationRepository=stationRepository;

    }


    @Override
    public int getCount() {
        return stationList.size();
    }
    @Override
    public void add(@Nullable Station object) {
        stationList.add(object);
        super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Station> collection) {
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
            convertView = layoutInflater.inflate(R.layout.station_item, parentView, false);
            if (position % 2 == 0) {
                convertView.setBackgroundResource(R.color.gray_darker);
            } else {
                convertView.setBackgroundResource(R.color.gray);
            }
            // Get the parts
            TextView name = (TextView) convertView.findViewById(R.id.fuel_supplier_name_textView);
            TextView type = (TextView) convertView.findViewById(R.id.fuel_type_textView);
            TextView amount = (TextView) convertView.findViewById(R.id.amount_textView);
            TextView cost = (TextView) convertView.findViewById(R.id.cost_textView);
            ImageView overflow = (ImageView) convertView.findViewById(R.id.song_duration);

            // Set the parts
            Station station = stationList.get(position);
            name.setText(station.getName());
            type.setText(station.getTypeOfFuel());
            amount.setText("Quantity: "+station.getQuantity());
            cost.setText("Cost: "+station.getCost());
            overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChoiceMenu(v, position);
                }
            });

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
    private void showChoiceMenu(View view, final int pos) {
        final PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
        final Station station = stationList.get(pos);

        // Handle individual clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.station_edit: {
                        Intent intent = new Intent(activity, RedactorMapActivity.class);
                        intent.putExtra(Constants.IS_EDIT_KEY, stationList.get(pos));
                        activity.startActivity(intent);
                        return true;
                    }
                    case R.id.station_delete: {
                        if (!stationList.get(pos).isSynced()){
                            stationRepository.deleteTask(station);
                            ((MainActivity) activity).updateContent();
                        } else {
                            stationList.get(pos).setDeleted(true);
                            stationRepository.updateTask(station);
                            ((MainActivity) activity).updateContent();

                        }
                        return true;
                    }

                }

                return false;
            }
        });

        // Create menu and show
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.choice_menu_station, popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    public void clear() {
        stationList.clear();
        super.clear();
    }

}
