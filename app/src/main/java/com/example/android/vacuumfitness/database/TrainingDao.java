package com.example.android.vacuumfitness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
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
