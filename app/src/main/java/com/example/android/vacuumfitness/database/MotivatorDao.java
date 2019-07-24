package com.example.android.vacuumfitness.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.example.android.vacuumfitness.model.Motivator;
import java.util.List;

@Dao
public interface MotivatorDao {

    @Insert
    void insertAll(Motivator... motivators);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceAll(List<Motivator> motivators);

    @Query("SELECT * FROM motivators WHERE primaryKey = :id")
    Motivator loadMotivatorById(int id);

    @Query("SELECT COUNT(motivation_text) FROM motivators")
    int getMotivatorsRowCount();
}
