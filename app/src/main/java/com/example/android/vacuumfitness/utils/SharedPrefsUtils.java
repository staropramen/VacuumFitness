package com.example.android.vacuumfitness.utils;

import android.content.SharedPreferences;

import com.example.android.vacuumfitness.ui.MainActivity;

import java.util.List;

public class SharedPrefsUtils {

    private static String EXERCISE_IDS = "exercise-ids";
    private static String IS_FIRST_RUN = "is-first-run";
    private static String EXO_PLAYER_POSITION = "exo-player-position";
    private static String PLAYLIST_ID = "playlist-id";
    private static String MUSIC_SPINNER_POSITION = "music-spinner-position";
    private static String TRAINING_SPINNER_POSITION = "training-spinner-position";
    private static String LEVEL_SPINNER_POSITION = "level-spinner-position";

    private static SharedPreferences sharedPreferences = MainActivity.sharedPreferences;

    public static void saveExerciseIdsToSharedPrefs(List<Integer> exerciseIds){
        String exerciseIdsString = ListConverter.fromList(exerciseIds);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EXERCISE_IDS, exerciseIdsString);
        editor.apply();
    }

    public static List<Integer> getExerciseIdsFromSharedPrefs(){
        String exerciseIdsString = sharedPreferences.getString(EXERCISE_IDS, "[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]");
        return ListConverter.fromString(exerciseIdsString);
    }

    public static void saveIsFirstRunToPrefs(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_FIRST_RUN, false);
        editor.apply();
    }

    public static boolean getIsFirstRun(){
        Boolean isFirstRun = sharedPreferences.getBoolean(IS_FIRST_RUN, true);
        return isFirstRun;
    }

    public static void saveExoPlayerPosition(long position){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(EXO_PLAYER_POSITION, position);
        editor.apply();
    }

    public static long getExoPlayerPosition(){
        long position = sharedPreferences.getLong(EXO_PLAYER_POSITION, 0);
        return position;
    }

    public static void savePlaylistId(int id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PLAYLIST_ID, id);
        editor.apply();
    }

    public static int getPlaylistId(){
        int id = sharedPreferences.getInt(PLAYLIST_ID, -1);
        return id;
    }

    public static void saveMusicSpinnerPosition(int pos){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MUSIC_SPINNER_POSITION, pos);
        editor.apply();
    }

    public static int getMusicSpinnerPosition(){
        int pos = sharedPreferences.getInt(MUSIC_SPINNER_POSITION, 1);
        return pos;
    }

    public static void saveTrainingSpinnerPosition(int pos){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TRAINING_SPINNER_POSITION, pos);
        editor.apply();
    }

    public static int getTrainingSpinnerPosition(){
        int pos = sharedPreferences.getInt(TRAINING_SPINNER_POSITION, 0);
        return pos;
    }

    public static void saveLevelSpinnerPosition(int pos){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LEVEL_SPINNER_POSITION, pos);
        editor.apply();
    }

    public static int getLevelSpinnerPosition(){
        int pos = sharedPreferences.getInt(LEVEL_SPINNER_POSITION, 0);
        return pos;
    }
}
