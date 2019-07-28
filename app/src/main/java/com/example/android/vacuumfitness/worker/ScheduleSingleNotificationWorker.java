package com.example.android.vacuumfitness.worker;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class ScheduleSingleNotificationWorker extends Worker {

    private static String WORK_TAG = "single_notification_work_tag";

    public ScheduleSingleNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        setupWork();
        return Result.success();
    }

    private void setupWork() {
        //Cancel old work thaht may still be there
        WorkManager.getInstance().cancelAllWorkByTag(WORK_TAG);

        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(SendNotificationWorker.class)
                        .setInitialDelay(46, TimeUnit.HOURS)
                        .addTag(WORK_TAG)
                        .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
    }
}
