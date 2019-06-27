package com.example.android.vacuumfitness.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;

import java.util.List;


public class ExerciseListViewModel extends ViewModel {

    private LiveData<List<Exercise>> exercises;
    private static String LOG_TAG = ExerciseListViewModel.class.getSimpleName();

    public LiveData<List<Exercise>> getExercises(Context context, List<Integer> ids){
        if(exercises == null){
            loadAllMovies(context, ids);
        }
        return exercises;
    }

    private void loadAllMovies(Context context, List<Integer> ids){
        AppDatabase db = AppDatabase.getInstance(context);
        Log.d(LOG_TAG, "Load exercises by ids from DB");
        exercises = db.exerciseDao().getTrainingExercises(ids);
    }
}
