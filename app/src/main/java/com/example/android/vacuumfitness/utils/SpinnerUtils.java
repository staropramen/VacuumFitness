package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;
import com.google.common.io.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SpinnerUtils {

    public static int getDaysDaySpinnerPosition(Context context, List<Training> list) {
        String dayString = getDayString(context);
        int i = 0;
        while (true) {
            if (i >= list.size()) {
                i = -1;
                break;
            } else if (dayString.equals(list.get(i).getLabel())) {
                break;
            } else {
                i++;
            }
        }
        return i;
    }

    private static String getDayString(Context context) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayString = "";

        switch (day) {
            case Calendar.MONDAY:
                dayString = context.getString(R.string.mon_label);
                break;
            case Calendar.TUESDAY:
                dayString = context.getString(R.string.tue_label);
                break;
            case Calendar.WEDNESDAY:
                dayString = context.getString(R.string.wed_label);
                break;
            case Calendar.THURSDAY:
                dayString = context.getString(R.string.thu_label);
                break;
            case Calendar.FRIDAY:
                dayString = context.getString(R.string.fri_label);
                break;
            case Calendar.SATURDAY:
                dayString = context.getString(R.string.sat_label);
                break;
            case Calendar.SUNDAY:
                dayString = context.getString(R.string.sun_label);
                break;
        }

        return dayString;
    }

    //Function Populates the MusicSpinner and fills the List of all Playlists
    public static List<Playlist> populateMusicSpinnerItems(Context context, Spinner musicSpinner, List<Playlist> dbPlaylists){

        List<Playlist> allPlaylists = new ArrayList<>();

        //Pre populate list with default items
        List<String> itemList = new ArrayList<>();
        //No Music
        Playlist noMusicPlaylist = new Playlist(-1, context.getString(R.string.no_music));
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
        training.setTrainingName(context.getString(R.string.random_training));
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
