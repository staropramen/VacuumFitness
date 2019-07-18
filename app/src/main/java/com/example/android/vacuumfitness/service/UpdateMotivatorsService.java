package com.example.android.vacuumfitness.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateMotivatorsService extends Service {

    private static String LOG_TAG = UpdateMotivatorsService.class.getSimpleName();

    public UpdateMotivatorsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service Started");
        return super.onStartCommand(intent, flags, startId);
    }
}
