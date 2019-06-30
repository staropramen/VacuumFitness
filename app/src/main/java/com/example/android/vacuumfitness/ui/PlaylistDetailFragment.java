package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.MusicUtils;
import com.example.android.vacuumfitness.viewmodel.SinglePlaylistViewModel;

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
    private List<Song> mSongs;
    private LinearLayoutManager mLayoutManager;
    private SongAdapter mSongAdapter;
    @BindView(R.id.rv_playlist_items) RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_playlist) TextView mEmptyPlaylistTextView;
    @BindView(R.id.tv_playlist_count) TextView mPlaylistCountTextView;
    @BindView(R.id.fab_add_song) FloatingActionButton mFab;
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

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.PLAYLIST_ID_KEY)){
            id = data.getLong(KeyUtils.PLAYLIST_ID_KEY);
        } else {
            id = -1;
        }

        //Setup toolbar
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        setupExistingPlaylist();

        return rootView;
    }

    @Override
    public void onClick(Song song) {

    }

    private void setupExistingPlaylist(){
        SinglePlaylistViewModel viewModel = ViewModelProviders.of(this).get(SinglePlaylistViewModel.class);
        viewModel.getPlaylist(getActivity(), id).observe(this, new Observer<Playlist>() {
            @Override
            public void onChanged(@Nullable Playlist playlist) {

                //Set Title
                mToolbar.setTitle(playlist.getPlaylistName());

                mSongs = playlist.getSongList();
                if(mSongs == null|| mSongs.isEmpty() ){
                    //Set exercises Count to zero
                    mPlaylistCountTextView.setText(String.valueOf(0));
                    //Set Time to zero
                    mPlaylistDurationTextView.setText(getString(R.string.zero_time));
                    //Show empty view
                    mEmptyPlaylistTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    //Set exercises count based on list size
                    mPlaylistCountTextView.setText(String.valueOf(mSongs.size()));

                    //Set playlist duration based on list
                    mPlaylistDurationTextView.setText(MusicUtils.getPlaylistDuration(mSongs));

                    mEmptyPlaylistTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mSongAdapter.setSongs(mSongs);
                }

            }
        });
    }
}
