package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SpinnerUtils {

    //Function Populates the MusicSpinner and fills the List of all Playlists
    public static void populateMusicSpinnerItems(Context context, Spinner musicSpinner, List<Playlist> allPlaylists, List<Playlist> dbPlaylists){

        //Pre populate list with default items
        List<String> itemList = new ArrayList<>();
        //No Music
        Playlist noMusicPlaylist = new Playlist(-1, "No Music");
        itemList.add(noMusicPlaylist.getPlaylistName());
        allPlaylists.add(noMusicPlaylist);

        //App music 1
        List<Song> songs = new ArrayList<>();
        Song relaxSong = new Song("dummymusic", "Various Artists", "Relax", "2983000");
        songs.add(relaxSong);
        Playlist relaxingPlaylist = new Playlist("Relaxing Music", songs, false);
        itemList.add(relaxingPlaylist.getPlaylistName());
        allPlaylists.add(relaxingPlaylist);

        //Add Playlists to itemList
        for (int i = 0; i < dbPlaylists.size(); i++){
            Playlist currentPlaylist = dbPlaylists.get(i);
            String playlistName = currentPlaylist.getPlaylistName();
            itemList.add(playlistName);
            allPlaylists.add(currentPlaylist);
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, itemList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        musicSpinner.setAdapter(adapter);

    }

    //Same Function Populates the MusicSpinner but dont take a List of all Playlists as argument
    public static void populateMusicSpinnerItems(Context context, Spinner musicSpinner, List<Playlist> dbPlaylists){

        //Pre populate list with default items
        List<String> itemList = new ArrayList<>();
        //No Music
        Playlist noMusicPlaylist = new Playlist(-1, "No Music");
        itemList.add(noMusicPlaylist.getPlaylistName());

        //App music 1
        List<Song> songs = new ArrayList<>();
        Song relaxSong = new Song("dummymusic", "Various Artists", "Relax", "2983000");
        songs.add(relaxSong);
        Playlist relaxingPlaylist = new Playlist("Relaxing Music", songs, false);
        itemList.add(relaxingPlaylist.getPlaylistName());

        //Add Playlists to itemList
        for (int i = 0; i < dbPlaylists.size(); i++){
            Playlist currentPlaylist = dbPlaylists.get(i);
            String playlistName = currentPlaylist.getPlaylistName();
            itemList.add(playlistName);
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, itemList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        musicSpinner.setAdapter(adapter);

    }
}
