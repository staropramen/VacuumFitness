package com.example.android.vacuumfitness.utils;

import android.content.SharedPreferences;

import com.example.android.vacuumfitness.ui.MainActivity;

import java.util.List;

public class SharedPrefsUtils {

    private static String EXERCISE_IDS = "exercise-ids";
    private static String IS_FIRST_RUN = "is-first-run";

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

}
