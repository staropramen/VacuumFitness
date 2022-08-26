package com.example.android.vacuumfitness.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Playlist;

public class SinglePlaylistViewModel extends ViewModel {

    private static String LOG_TAG = SingleTrainingViewModel.class.getSimpleName();

    private LiveData<Playlist> playlist;

    public LiveData<Playlist> getPlaylist(Context context, long playlistId){
        if(playlist == null){
            loadData(context, playlistId);
        }
        return playlist;
    }

    private void loadData(Context context, long playlistId){
        AppDatabase db = AppDatabase.getInstance(context);
        Log.d(LOG_TAG, "Load data from database");
        playlist = db.playlistDao().loadPlaylistById(playlistId);
    }
}
