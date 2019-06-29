package com.example.android.vacuumfitness.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Query("SELECT * FROM playlist ORDER BY primaryKey")
    LiveData<List<Playlist>> loadAllPlaylist();

    @Query("SELECT * FROM playlist WHERE primaryKey = :id")
    LiveData<Playlist> loadPlaylistById(long id);

    @Insert
    long insertPlaylist(Playlist playlist);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlaylist(Playlist playlist);

    @Delete
    void deletePlaylist(Playlist playlist);
}
