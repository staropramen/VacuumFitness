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
import com.example.android.vacuumfitness.adapter.SongAdapter;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.MusicUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMusicFragment extends Fragment implements SongAdapter.SongClickHandler {

    private List<Song> songs;

    private LinearLayoutManager layoutManager;
    private SongAdapter songAdapter;
    @BindView(R.id.rv_songs_view) RecyclerView songsRecyclerView;
    //@BindView(R.id.tv_empty_trainings_list) TextView emptyListTextView;
    //@BindView(R.id.fab_add_training) FloatingActionButton fabAddTraining;

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

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        songsRecyclerView.setLayoutManager(layoutManager);
        songAdapter = new SongAdapter(this);
        songsRecyclerView.setAdapter(songAdapter);

        //Check Permisson and Requast if its necessary
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }

        setupSongAdapter();

        return rootView;
    }

    private void setupSongAdapter(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                songs = MusicUtils.getSongList(getActivity());

                songAdapter.setSongs(songs);
            }
        });
    }


    ConcatenatingMediaSource playlist = new ConcatenatingMediaSource();

    @Override
    public void onClick(Song song) {
        Uri uri = Uri.parse(song.getPath());
        String userAgent = Util.getUserAgent(getActivity(), KeyUtils.EXO_VIDEO_PLAYER);
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        playlist.addMediaSource(mediaSource);
    }
}
