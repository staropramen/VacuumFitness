package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "playlist")
public class Playlist implements Parcelable {

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

    @Ignore
    public Playlist(String playlistName, List<Song> songList) {
        this.playlistName = playlistName;
        this.songList = songList;
    }

    //Parcelable Constructor
    @Ignore
    public Playlist(Parcel in){
        primaryKey = in.readInt();
        playlistName = in.readString();
        songList = new ArrayList<>();
        in.readList(songList, Song.class.getClassLoader());
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryKey);
        dest.writeString(playlistName);
        dest.writeList(songList);
    }

    @Ignore
    public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}
