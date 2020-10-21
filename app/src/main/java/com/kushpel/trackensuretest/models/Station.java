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
package com.kushpel.trackensuretest.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity
public class Station implements Parcelable {
    @PrimaryKey(autoGenerate=true)
    private long id;
    private String name;
    private String typeOfFuel;
    private double quantity;
    private double price;
    private long statisticsId;
    private boolean deleted;
    private boolean synced;


    public Station (String name, String typeOfFuel, double quantity, double price, long statisticsId){
        this.name = name;
        this.typeOfFuel = typeOfFuel;
        this.quantity = quantity;
        this.price = price;
        this.statisticsId = statisticsId;
        this.synced = false;
        this.deleted = false;
    }

    public Station() {
    }

    protected Station(Parcel in) {
        id = in.readLong();
        name = in.readString();
        typeOfFuel = in.readString();
        quantity = in.readDouble();
        price = in.readDouble();
        statisticsId = in.readLong();
        synced = in.readByte() != 0;
        deleted = in.readByte() != 0;
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfFuel() {
        return typeOfFuel;
    }

    public void setTypeOfFuel(String typeOfFuel) {
        this.typeOfFuel = typeOfFuel;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return price * quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(long StatisticsId) {
        this.statisticsId = statisticsId;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(typeOfFuel);
        dest.writeDouble(quantity);
        dest.writeDouble(price);
        dest.writeLong(statisticsId);
        dest.writeByte((byte) (synced ? 1 : 0));
        dest.writeByte((byte) (deleted ? 1 : 0));
    }



}
