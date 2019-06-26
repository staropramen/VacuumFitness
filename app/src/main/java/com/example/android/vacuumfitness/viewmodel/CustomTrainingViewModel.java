package com.example.android.vacuumfitness.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Training;

import java.util.List;

public class CustomTrainingViewModel extends AndroidViewModel {

    private static String LOG_TAG = CustomTrainingViewModel.class.getSimpleName();

    private LiveData<List<Training>> trainings;

    public CustomTrainingViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Load trainings from DB");
        trainings = db.trainingDao().loadAllTrainings();
    }

    public LiveData<List<Training>> getTrainings(){return trainings;}
}
