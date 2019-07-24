package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;

import java.util.ArrayList;
import java.util.List;

public class SpinnerUtils {

    //Function Populates the MusicSpinner and fills the List of all Playlists
    public static List<Playlist> populateMusicSpinnerItems(Context context, Spinner musicSpinner, List<Playlist> dbPlaylists){

        List<Playlist> allPlaylists = new ArrayList<>();

        //Pre populate list with default items
        List<String> itemList = new ArrayList<>();
        //No Music
        Playlist noMusicPlaylist = new Playlist(-1, "No Music");
        itemList.add(noMusicPlaylist.getPlaylistName());
        allPlaylists.add(noMusicPlaylist);

        //Add the defaul playlists
        itemList.add(MusicUtils.getDeepBluePlaylist().getPlaylistName());
        allPlaylists.add(MusicUtils.getDeepBluePlaylist());

        itemList.add(MusicUtils.getRelaxingPlaylist().getPlaylistName());
        allPlaylists.add(MusicUtils.getRelaxingPlaylist());

        itemList.add(MusicUtils.getSlowMotionPlaylist().getPlaylistName());
        allPlaylists.add(MusicUtils.getSlowMotionPlaylist());

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

        return allPlaylists;
    }

    public static List<Training> populateTrainingSpinnerItems(Context context, Spinner trainingSpinner, List<Training> trainings){

        Training training = new Training();
        training.setTrainingName("Random Training");
        training.setPrimaryKey(-1);

        List<Training> trainingList = new ArrayList<>();
        List<String> itemList = new ArrayList<>();
        itemList.add(training.getTrainingName());
        trainingList.add(training);

        // Add trainings to list
        for (int i = 0; i < trainings.size(); i++){
            Training currentTraining = trainings.get(i);
            String trainingName = currentTraining.getTrainingName();
            itemList.add(trainingName);
            trainingList.add(currentTraining);
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, itemList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        trainingSpinner.setAdapter(adapter);

        return trainingList;
    }

}
