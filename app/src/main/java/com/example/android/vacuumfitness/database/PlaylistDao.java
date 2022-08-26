package com.example.android.vacuumfitness.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android.vacuumfitness.model.Playlist;

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
