package com.example.android.vacuumfitness.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.utils.RandomTrainingUtils;

import java.util.ArrayList;
import java.util.List;

public class TrainingViewModel extends ViewModel {

    private static String LOG_TAG = TrainingViewModel.class.getSimpleName();

    private LiveData<List<Exercise>> exercises;

    public LiveData<List<Exercise>> getExercises(Context context, List<Integer> idList){
        if(exercises == null){
            loadData(context, idList);
        }
        return exercises;
    }

    private void loadData(Context context, List<Integer> idList){
        AppDatabase db = AppDatabase.getInstance(context);
        Log.d(LOG_TAG, "Load data from database");
        exercises = db.exerciseDao().getTrainingExercises(idList);
    }
}
