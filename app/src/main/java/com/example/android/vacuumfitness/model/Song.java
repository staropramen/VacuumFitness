package com.example.android.vacuumfitness.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    //Path of the Song
    private String path;

    // Artist of the Song
    private String songArtist;

    // Name of the Song
    private String songName;

    // Length of the song
    private String songLength;

    public Song(String path, String songArtist, String songName, String songLength) {
        this.path = path;
        this.songArtist = songArtist;
        this.songName = songName;
        this.songLength = songLength;
    }

    //Empty Constructor
    public Song(){
    }

    //Constructor for Parcelable
    public Song(Parcel in){
        path = in.readString();
        songArtist = in.readString();
        songName = in.readString();
        songLength = in.readString();
    }

    public String getPath() {
        return path;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongLength() {
        return songLength;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSongLength(String songLength) {
        this.songLength = songLength;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(songArtist);
        dest.writeString(songName);
        dest.writeString(songLength);
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.songName != null ? this.songName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Song.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Song other = (Song) obj;
        if ((this.songName == null) ? (other.songName != null) : !this.songName.equals(other.songName)) {
            return false;
        }

        return true;
    }
}
