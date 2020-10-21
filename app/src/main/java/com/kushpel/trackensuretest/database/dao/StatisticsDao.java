package com.kushpel.trackensuretest.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.kushpel.trackensuretest.models.Statistics;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface StatisticsDao {
    @Query("SELECT * FROM Statistics")
    LiveData<List<Statistics>> getAll();

    @Query("SELECT * FROM Statistics WHERE synced = 0")
    LiveData<List<Statistics>> getNotSynced();

    @Query("DELETE FROM Statistics")
    void deleteAll();

    @Query("SELECT * FROM Statistics WHERE name = :name")
    LiveData<Statistics> getByName(String name);

    @Query("SELECT * FROM Statistics WHERE id = :id")
    LiveData<Statistics> getById(long id);

    @Insert(onConflict = REPLACE)
    void insert(Statistics... statistics);

    @Update
    void update(Statistics... statistics);

    @Delete
    void delete(Statistics... statistics);
}
