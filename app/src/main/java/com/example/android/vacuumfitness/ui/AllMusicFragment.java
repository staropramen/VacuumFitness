package com.example.android.vacuumfitness.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.SongAdapter;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.MusicUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMusicFragment extends Fragment implements SongAdapter.SongClickHandler{

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

    @Override
    public void onClick(Song song) {

    }
}
