package com.kushpel.trackensuretest.database.dao;





import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kushpel.trackensuretest.models.Station;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface StationDao {
    @Query("SELECT * FROM Station WHERE deleted = 0")
    LiveData<List<Station>> getAll();

    @Query("SELECT * FROM Station WHERE synced = 0 AND deleted = 0")
    LiveData<List<Station>> getNotSynced();

    @Query("SELECT * FROM Station WHERE deleted = 1")
    LiveData<List<Station>> getDeleted();

    @Query("DELETE FROM Station")
    void deleteAll();

    @Insert(onConflict = REPLACE)
    void insert(Station... stations);

    @Update
    void update(Station... stations);

    @Delete
    void delete(Station... stations);
}
