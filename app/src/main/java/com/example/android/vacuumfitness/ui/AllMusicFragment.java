package com.example.android.vacuumfitness.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.ChooseSongAdapter;
import com.example.android.vacuumfitness.adapter.SongAdapter;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.MusicUtils;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMusicFragment extends Fragment implements ChooseSongAdapter.ChooseSongClickHandler {

    private List<Song> songs;
    private List<Song> mChosenSongs;
    private Playlist mPlaylist;
    private LinearLayoutManager layoutManager;
    private ChooseSongAdapter songAdapter;
    @BindView(R.id.rv_songs_view) RecyclerView songsRecyclerView;

    public AllMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_music, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.all_music);

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.PLAYLIST_KEY)){
            mPlaylist = data.getParcelable(KeyUtils.PLAYLIST_KEY);
        } else {
            mPlaylist = null;
        }

        //Get the list of existing songs
        if(mPlaylist.getSongList() == null){
            mChosenSongs = new ArrayList<>();
        }else {
            mChosenSongs = mPlaylist.getSongList();
        }

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        songsRecyclerView.setLayoutManager(layoutManager);
        songAdapter = new ChooseSongAdapter(this, mChosenSongs, getActivity());
        songsRecyclerView.setAdapter(songAdapter);

        setupSongAdapter();

        return rootView;
    }

    private void setupSongAdapter(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                songs = MusicUtils.getSongList(getActivity());

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        songAdapter.setSongs(songs);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(Song song) {

    }
}
