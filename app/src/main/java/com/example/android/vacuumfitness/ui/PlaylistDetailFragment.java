package com.example.android.vacuumfitness.ui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.ExerciseAdapter;
import com.example.android.vacuumfitness.adapter.SongAdapter;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetailFragment extends Fragment implements SongAdapter.SongClickHandler {

    private Toolbar mToolbar;
    private long id;
    private Playlist mPlaylist;
    private List<Song> songs;
    private LinearLayoutManager mLayoutManager;
    private SongAdapter mSongAdapter;
    @BindView(R.id.rv_playlist_items) RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_playlist) TextView mEmptyPlaylistTextView;
    @BindView(R.id.tv_playlist_count) TextView mPlaylistCountTextView;
    @BindView(R.id.fab_add_exercise) FloatingActionButton mFab;
    @BindView(R.id.tv_duration_count) TextView mPlaylistDurationTextView;


    public PlaylistDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist_detail, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Prepare RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSongAdapter = new SongAdapter(this);
        mRecyclerView.setAdapter(mSongAdapter);

        //Setup toolbar
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        return rootView;
    }

    @Override
    public void onClick(Song song) {

    }
}
