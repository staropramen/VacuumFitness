package com.example.android.vacuumfitness.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class MotivationTextService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MotivationTextViewsFactory(getApplicationContext());
    }
}

class MotivationTextViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static String LOG_TAG = MotivationTextViewsFactory.class.getSimpleName();
    private Context mContext;

    //Constructor
    public MotivationTextViewsFactory(Context applicationContext){mContext = applicationContext;};

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "On Create Service called");
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
