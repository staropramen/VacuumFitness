package com.example.android.vacuumfitness.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.vacuumfitness.model.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY primaryKey")
    LiveData<List<Exercise>> loadAllExercises();

    @Insert
    void insertExercise(Exercise exercise);

    @Insert
    void insertAll(Exercise... exercises);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Query("SELECT * FROM exercises WHERE primaryKey = :id")
    LiveData<Exercise> loadExerciseById(int id);

    @Query("SELECT COUNT(exercise_name) FROM exercises")
    int getExerciseRowCount();
}
