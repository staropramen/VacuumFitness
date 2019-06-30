package com.example.android.vacuumfitness.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Playlist;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private static String LOG_TAG = PlaylistViewModel.class.getSimpleName();

    private LiveData<List<Playlist>> playlists;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Load playlists from DB");
        playlists = db.playlistDao().loadAllPlaylist();
    }

    public LiveData<List<Playlist>> getPlaylists(){return playlists;}
}
