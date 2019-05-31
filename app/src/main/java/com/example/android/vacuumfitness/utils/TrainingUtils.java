package com.example.android.vacuumfitness.utils;

import java.util.Random;

public class TrainingUtils {

    public static int getRandomExercisesArray(int maxNumber){
        maxNumber = maxNumber++;

        int random = new Random().nextInt(maxNumber);

        return random;
    }

}
