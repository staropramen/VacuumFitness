package com.example.android.vacuumfitness.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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

public class UpdateMotivatorsServiceOld extends Service {

    private static String LOG_TAG = UpdateMotivatorsServiceOld.class.getSimpleName();
    private List<Motivator> mMotivators;

    public UpdateMotivatorsServiceOld() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service Started");

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
                        AppDatabase.getInstance(getApplicationContext()).motivatorDao().insertOrReplaceAll(mMotivators);
                    }
                });

                //Now the size of mMotivator is equal rowCount so lets save this to shared prefs
                SharedPrefsUtils.saveMotivatorsRowCount(getApplicationContext(), mMotivators.size());

            }

            @Override
            public void onFailure(Call<List<Motivator>> call, Throwable t) {
                t.printStackTrace();
                Log.d(LOG_TAG, "Api call failed");
            }
        });


        //As all is done we stop the service
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Service was destroyed");
    }
}
