package com.example.android.vacuumfitness.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
}
