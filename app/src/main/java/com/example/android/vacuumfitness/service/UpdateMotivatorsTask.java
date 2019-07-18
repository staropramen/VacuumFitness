package com.example.android.vacuumfitness.service;

import android.content.Context;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.network.GetDataService;
import com.example.android.vacuumfitness.network.RetrofitClientInstance;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMotivatorsTask {

    public static final String ACTION_UPDATE_MOTIVATORS = "action-update-motivators";

    private static String LOG_TAG = UpdateMotivatorsTask.class.getSimpleName();
    private static List<Motivator> mMotivators;

    public static void executeTasks(Context context, String action) {
        if(ACTION_UPDATE_MOTIVATORS.equals(action)){
            updateMotivators(context);
        }

    }

    private static void updateMotivators(final Context context) {
        GetDataService api = RetrofitClientInstance.getApiService();
        Call<List<Motivator>> call = api.getAllMotivators();

        call.enqueue(new Callback<List<Motivator>>() {
            @Override
            public void onResponse(Call<List<Motivator>> call, Response<List<Motivator>> response) {
                Log.d(LOG_TAG, "Successful api call");
                mMotivators = response.body();

                //Insert or replace in db
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(context).motivatorDao().insertOrReplaceAll(mMotivators);
                    }
                });

                //Now the size of mMotivator is equal rowCount so lets save this to shared prefs
                SharedPrefsUtils.saveMotivatorsRowCount(context, mMotivators.size());

            }

            @Override
            public void onFailure(Call<List<Motivator>> call, Throwable t) {
                t.printStackTrace();
                Log.d(LOG_TAG, "Api call failed");
            }
        });
    }
}
