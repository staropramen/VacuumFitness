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
    //Specifies if playlist is from user or from app by default true
    @ColumnInfo(name = "is-custom")
    private boolean isCustom = true;

    public Playlist(int primaryKey, String playlistName, List<Song> songList, boolean isCustom) {
        this.primaryKey = primaryKey;
        this.playlistName = playlistName;
        this.songList = songList;
        this.isCustom = isCustom;
    }

    //Empty Constructor
    @Ignore
    public Playlist() {}

    @Ignore
    public Playlist(String playlistName, List<Song> songList, boolean isCustom) {
        this.playlistName = playlistName;
        this.songList = songList;
        this.isCustom = isCustom;
    }

    @Ignore
    public Playlist(String playlistName, List<Song> songList) {
        this.playlistName = playlistName;
        this.songList = songList;
    }

    //Constructor for Playlist without Music
    @Ignore
    public Playlist(int primaryKey, String playlistName) {
        this.playlistName = playlistName;
        this.primaryKey = primaryKey;
    }

    //Parcelable Constructor
    @Ignore
    public Playlist(Parcel in){
        primaryKey = in.readInt();
        playlistName = in.readString();
        songList = new ArrayList<>();
        in.readList(songList, Song.class.getClassLoader());
        isCustom = in.readByte() != 0;
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

    public boolean isCustom() {
        return isCustom;
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

    public void setCustom(boolean custom) {
        isCustom = custom;
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
        dest.writeByte((byte) (isCustom ? 1 : 0));
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
