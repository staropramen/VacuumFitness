package com.example.android.vacuumfitness.model;

public class Song {

    // Artist of the Song
    private String songArtist;

    // Name of the Song
    private String songName;

    // Length of the song
    private String songLength;

    //Data of the Song
    private long data;

    public Song(String songArtist, String songName, String songLength, long data) {
        this.songArtist = songArtist;
        this.songName = songName;
        this.songLength = songLength;
        this.data = data;
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

    public long getData() {
        return data;
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

    public void setData(long data) {
        this.data = data;
    }
}
