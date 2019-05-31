package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTrainingUtils {

    private static List<Integer> idList = new ArrayList<>();

    public static List<Integer> getRandomExerciseIds(int exerciseCount) {
        List<Integer> randomList = new ArrayList<>();
        List<Integer> allExerciseList = SharedPrefsUtils.getExerciseIdsFromSharedPrefs();

        for(int i = 0; i < exerciseCount; i++){
            //Make Random number to get position
            Random random = new Random();
            int idPos = random.nextInt(allExerciseList.size());

            //Get the random position from list and add to random list
            randomList.add(allExerciseList.get(idPos));

            //Now delete it from list to not repeat
            allExerciseList.remove(idPos);
        }

        return randomList;
    }

}
