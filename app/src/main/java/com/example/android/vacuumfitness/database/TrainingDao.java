package com.example.android.vacuumfitness.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Training;

import java.util.List;

@Dao
public interface TrainingDao {
    @Query("SELECT * FROM trainings ORDER BY primaryKey")
    LiveData<List<Training>> loadAllTrainings();

    @Query("SELECT * FROM trainings WHERE primaryKey = :id")
    LiveData<Training> loadTrainingById(long id);

    @Insert
    long insertTraining(Training training);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTraining(Training training);

    @Delete
    void deleteTraining(Training training);
}
