package com.example.android.vacuumfitness.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

public class UpdateMotivatorsService extends IntentService {

    public UpdateMotivatorsService() {
        super("UpdateMotivatorsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            UpdateMotivatorsTask.executeTasks(this, action);
        }
    }


}
