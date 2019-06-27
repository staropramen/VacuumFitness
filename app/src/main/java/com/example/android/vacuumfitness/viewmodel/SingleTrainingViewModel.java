package com.example.android.vacuumfitness.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Training;

import java.util.List;

public class SingleTrainingViewModel extends ViewModel {

    private static String LOG_TAG = TrainingViewModel.class.getSimpleName();

    private LiveData<Training> training;

    public LiveData<Training> getTraining(Context context, long trainingId){
        if(training == null){
            loadData(context, trainingId);
        }
        return training;
    }

    private void loadData(Context context, long trainingId){
        AppDatabase db = AppDatabase.getInstance(context);
        Log.d(LOG_TAG, "Load data from database");
        training = db.trainingDao().loadTrainingById(trainingId);
    }
}
