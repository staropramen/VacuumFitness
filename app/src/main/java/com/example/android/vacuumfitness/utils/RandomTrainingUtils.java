package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Training;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTrainingUtils {

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

    public static List<Integer> makeIdListFromTraining(Training training, int exerciseCount){
        List<Integer> idList = new ArrayList<>();

        List<Exercise> exerciseList = training.getExerciseList();

        //Add primary keys to idList
        for(int i = 0; i < exerciseList.size(); i++){
            Exercise current = exerciseList.get(i);
            int id = current.getPrimaryKey();
            idList.add(id);
        }

        //Cut id List to the right size
        int itemsToRemove = exerciseCount - exerciseList.size();
        for (int j = 0; j < itemsToRemove; j++){
            idList.remove(idList.size() - 1);
        }

        return idList;
    }

}
