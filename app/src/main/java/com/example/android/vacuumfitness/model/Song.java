package com.example.android.vacuumfitness.model;

public class Song {

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
}
