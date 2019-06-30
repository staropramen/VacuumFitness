package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;

import java.util.List;

@Entity(tableName = "playlist")
public class Playlist {

    @PrimaryKey(autoGenerate = true)
    private int primaryKey;
    @ColumnInfo(name = "playlist-name")
    private String playlistName;
    @ColumnInfo(name = "song-list")
    private List<Song> songList;

    public Playlist(int primaryKey, String playlistName, List<Song> songList) {
        this.primaryKey = primaryKey;
        this.playlistName = playlistName;
        this.songList = songList;
    }

    //Empty Constructor
    @Ignore
    public Playlist() {}

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void setMediaSource(List<Song> songList) {
        this.songList = songList;
    }
}
