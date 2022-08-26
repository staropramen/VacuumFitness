package com.example.android.vacuumfitness.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;

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
