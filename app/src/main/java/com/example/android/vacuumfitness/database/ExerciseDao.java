package com.example.android.vacuumfitness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.android.vacuumfitness.model.Exercise;
import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY primaryKey")
    LiveData<List<Exercise>> loadAllExercises();

    @Insert
    void insertAll(Exercise... exercises);

    @Query("SELECT primaryKey FROM exercises ORDER BY primaryKey")
    List<Integer> loadExerciseIdArray();

    @Query("SELECT * FROM exercises WHERE primaryKey IN (:ids)")
    LiveData<List<Exercise>> getTrainingExercises(List<Integer> ids);
}
