package com.example.android.vacuumfitness.worker;

import android.content.Context;
import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.vacuumfitness.utils.NotificationUtils;

public class SendNotificationWorker extends Worker {

    private Context mContext;

    public SendNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationUtils.motivateUserForTraining(mContext);

        return Result.success();
    }
}
