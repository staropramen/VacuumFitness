package com.example.android.vacuumfitness.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.vacuumfitness.model.Motivator;

import java.util.List;

@Dao
public interface MotivatorDao {

    @Insert
    void insertMotivator(Motivator motivator);

    @Insert
    void insertAll(Motivator... motivators);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceAll(List<Motivator> motivators);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMotivator(Motivator motivator);

    @Delete
    void deleteMotivator(Motivator motivator);

    @Query("SELECT * FROM motivators WHERE primaryKey = :id")
    Motivator loadMotivatorById(int id);

    @Query("SELECT COUNT(motivation_text) FROM motivators")
    int getMotivatorsRowCount();
}
