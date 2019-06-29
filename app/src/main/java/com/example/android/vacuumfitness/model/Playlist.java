package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;

@Entity(tableName = "playlist")
public class Playlist {

    @PrimaryKey(autoGenerate = true)
    private int primaryKey;
    @ColumnInfo(name = "playlist-name")
    private String playlistName;
    @ColumnInfo(name = "media-source")
    private ConcatenatingMediaSource mediaSource;

    public Playlist(int primaryKey, String playlistName, ConcatenatingMediaSource mediaSource) {
        this.primaryKey = primaryKey;
        this.playlistName = playlistName;
        this.mediaSource = mediaSource;
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

    public ConcatenatingMediaSource getMediaSource() {
        return mediaSource;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void setMediaSource(ConcatenatingMediaSource mediaSource) {
        this.mediaSource = mediaSource;
    }
}
