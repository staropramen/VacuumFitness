package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPrefsUtils {

    private static String EXERCISE_IDS = "exercise-ids";
    private static String IS_FIRST_RUN = "is-first-run";
    private static String EXO_PLAYER_POSITION = "exo-player-position";
    private static String EXO_PLAYER_INDEX = "exo-player-index";
    private static String PLAYLIST_ID = "playlist-id";
    private static String MUSIC_SPINNER_POSITION = "music-spinner-position";
    private static String TRAINING_SPINNER_POSITION = "training-spinner-position";
    private static String LEVEL_SPINNER_POSITION = "level-spinner-position";
    private static String DUCK_MUSIC_BOOLEAN = "duck-music-boolean";
    private static String VOICE_TOGGLE_BOOLEAN = "voice-toggle-boolean";
    private static String VISUAL_TOGGLE_BOOLEAN = "visual-toggle-boolean";
    private static String MOTIVATORS_ROW_COUNT = "motivators-row-count";
    private static String LAST_MOTIVATOR_ID = "last-motivator_id";
    private static String CURRENT_MOTIVATOR = "current-motivator";

    public static void saveExerciseIdsToSharedPrefs(Context context, List<Integer> exerciseIds){
        String exerciseIdsString = ListConverter.fromList(exerciseIds);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(EXERCISE_IDS, exerciseIdsString);
        editor.apply();
    }

    public static List<Integer> getExerciseIdsFromSharedPrefs(Context context){
        String exerciseIdsString = PreferenceManager.getDefaultSharedPreferences(context).getString(EXERCISE_IDS, "[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]");
        return ListConverter.fromString(exerciseIdsString);
    }

    public static void saveIsFirstRunToPrefs(Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(IS_FIRST_RUN, false);
        editor.apply();
    }

    public static boolean getIsFirstRun(Context context){
        Boolean isFirstRun = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_FIRST_RUN, true);
        return isFirstRun;
    }

    public static void saveExoPlayerIndex(Context context, int index){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(EXO_PLAYER_INDEX, index);
        editor.apply();
    }

    public static int getExoPlayerIndex(Context context){
        int index = PreferenceManager.getDefaultSharedPreferences(context).getInt(EXO_PLAYER_INDEX, 0);
        return index;
    }

    public static void saveExoPlayerPosition(Context context, long position){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(EXO_PLAYER_POSITION, position);
        editor.apply();
    }

    public static long getExoPlayerPosition(Context context){
        long position = PreferenceManager.getDefaultSharedPreferences(context).getLong(EXO_PLAYER_POSITION, 0);
        return position;
    }

    public static void savePlaylistId(Context context, int id){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(PLAYLIST_ID, id);
        editor.apply();
    }

    public static int getPlaylistId(Context context){
        int id = PreferenceManager.getDefaultSharedPreferences(context).getInt(PLAYLIST_ID, -1);
        return id;
    }

    public static void saveMusicSpinnerPosition(Context context, int pos){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(MUSIC_SPINNER_POSITION, pos);
        editor.apply();
    }

    public static int getMusicSpinnerPosition(Context context){
        int pos = PreferenceManager.getDefaultSharedPreferences(context).getInt(MUSIC_SPINNER_POSITION, 1);
        return pos;
    }

    public static void saveTrainingSpinnerPosition(Context context, int pos){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(TRAINING_SPINNER_POSITION, pos);
        editor.apply();
    }

    public static int getTrainingSpinnerPosition(Context context){
        int pos = PreferenceManager.getDefaultSharedPreferences(context).getInt(TRAINING_SPINNER_POSITION, 0);
        return pos;
    }

    public static void saveLevelSpinnerPosition(Context context, int pos){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(LEVEL_SPINNER_POSITION, pos);
        editor.apply();
    }

    public static int getLevelSpinnerPosition(Context context){
        int pos = PreferenceManager.getDefaultSharedPreferences(context).getInt(LEVEL_SPINNER_POSITION, 0);
        return pos;
    }

    public static void saveDuckMusicBoolean(Context context, boolean isChecked){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(DUCK_MUSIC_BOOLEAN, isChecked);
        editor.apply();
    }

    public static boolean getDuckMusicBoolean(Context context){
        boolean isChecked = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DUCK_MUSIC_BOOLEAN, true);
        return isChecked;
    }

    public static void saveVoiceToggleBoolean(Context context, boolean isOn){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(VOICE_TOGGLE_BOOLEAN, isOn);
        editor.apply();
    }

    public static boolean getVoiceToggleBoolean(Context context){
        boolean isOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(VOICE_TOGGLE_BOOLEAN, true);
        return isOn;
    }

    public static void saveVisualToggleBoolean(Context context, boolean isOn){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(VISUAL_TOGGLE_BOOLEAN, isOn);
        editor.apply();
    }

    public static boolean getVisualToggleBoolean(Context context){
        boolean isOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(VISUAL_TOGGLE_BOOLEAN, true);
        return isOn;
    }

    public static void saveMotivatorsRowCount(Context context, int rowCount){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(MOTIVATORS_ROW_COUNT, rowCount);
        editor.apply();
    }

    public static int getMotivatorsRowCount(Context context){
        int rowCount = PreferenceManager.getDefaultSharedPreferences(context).getInt(MOTIVATORS_ROW_COUNT, 1);
        return rowCount;
    }

    public static void saveLastMotivatorId(Context context, int id){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(LAST_MOTIVATOR_ID, id);
        editor.apply();
    }

    public static int getLastMotivatorId(Context context){
        int id = PreferenceManager.getDefaultSharedPreferences(context).getInt(LAST_MOTIVATOR_ID, 0);
        return id;
    }

    public static void saveCurrentMotivator(Context context, Motivator motivator){
        Gson gson = new Gson();
        String json = gson.toJson(motivator);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(CURRENT_MOTIVATOR, json);
        editor.apply();
    }

    public static Motivator getCurrentMotivator(Context context){
        String json = PreferenceManager.getDefaultSharedPreferences(context).getString(CURRENT_MOTIVATOR, getDefaultMotivatorString());
        Type type = new TypeToken<Motivator>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private static String getDefaultMotivatorString(){
        Motivator motivator = new Motivator(0, MainActivity.mDefaultMotivationText);
        Gson gson = new Gson();
        String json = gson.toJson(motivator);
        return json;
    }
}
