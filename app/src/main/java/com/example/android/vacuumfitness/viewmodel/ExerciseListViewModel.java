package com.example.android.vacuumfitness.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;

import java.util.List;

public class ExerciseListViewModel extends AndroidViewModel {

    private static String LOG_TAG = ExerciseListViewModel.class.getSimpleName();

    private LiveData<List<Exercise>> exercises;

    public ExerciseListViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Load trainings from DB");
        exercises = db.exerciseDao().loadAllExercises();
    }

    public LiveData<List<Exercise>> getExercises(){return exercises;}
}
