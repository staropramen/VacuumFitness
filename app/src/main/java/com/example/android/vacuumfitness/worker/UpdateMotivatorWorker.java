package com.example.android.vacuumfitness.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.vacuumfitness.R;
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

public class UpdateMotivatorWorker extends Worker {

    private static String LOG_TAG = UpdateMotivatorWorker.class.getSimpleName();
    private List<Motivator> mMotivators;
    private Context mContext;

    public UpdateMotivatorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        GetDataService api = RetrofitClientInstance.getApiService();
        Call<List<Motivator>> call = api.getAllMotivators(mContext.getString(R.string.motivators_json));

        call.enqueue(new Callback<List<Motivator>>() {
            @Override
            public void onResponse(Call<List<Motivator>> call, Response<List<Motivator>> response) {
                Log.d(LOG_TAG, "Successful api call");
                mMotivators = response.body();

                //Insert or replace in db
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(mContext).motivatorDao().insertOrReplaceAll(mMotivators);
                    }
                });

                //Now the size of mMotivator is equal rowCount so lets save this to shared prefs
                SharedPrefsUtils.saveMotivatorsRowCount(mContext, mMotivators.size());

            }
            @Override
            public void onFailure(Call<List<Motivator>> call, Throwable t) {
                t.printStackTrace();
                Log.d(LOG_TAG, "Api call failed");
            }
        });
        return Result.success();
    }
}
