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
import com.example.android.vacuumfitness.adapter.CustomTrainingAdapter;
import com.example.android.vacuumfitness.adapter.PlaylistAdapter;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomizeMusicFragment extends Fragment implements PlaylistAdapter.PlaylistAdapterOnClickHandler, PlaylistAdapter.PlaylistAdapterOnLongClickHandler {

    private LinearLayoutManager layoutManager;
    private PlaylistAdapter playlistAdapter;
    @BindView(R.id.rv_playlist) RecyclerView playlistRecyclerView;
    @BindView(R.id.tv_empty_playlist_list) TextView emptyListTextView;
    @BindView(R.id.fab_add_playlist) FloatingActionButton fab;

    public CustomizeMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customize_music, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.customize_music_title);

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        playlistRecyclerView.setLayoutManager(layoutManager);
        playlistAdapter = new PlaylistAdapter(this, this);
        playlistRecyclerView.setAdapter(playlistAdapter);

        return rootView;
    }

    @Override
    public void onClick(Playlist playlist) {

    }

    @Override
    public void onLongClick(Playlist playlist) {

    }
}
