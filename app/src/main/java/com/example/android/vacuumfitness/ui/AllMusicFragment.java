package com.example.android.vacuumfitness.ui;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.ChooseSongAdapter;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.MusicUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMusicFragment extends Fragment implements ChooseSongAdapter.ChooseSongClickHandler {

    private List<Song> mSongs;
    private List<Song> mChosenSongs;
    private Playlist mPlaylist;
    private LinearLayoutManager layoutManager;
    private ChooseSongAdapter songAdapter;
    boolean mShowSaveButton;
    @BindView(R.id.rv_songs_view) RecyclerView mSongsRecyclerView;
    @BindView(R.id.tv_no_music_on_device) TextView mNoMusicTextView;
    @BindView(R.id.fab_check) FloatingActionButton mSaveButton;

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

        //HIde Fab button
        mSaveButton.hide();
        mShowSaveButton = false;

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.PLAYLIST_KEY)){
            mPlaylist = data.getParcelable(KeyUtils.PLAYLIST_KEY);
        } else {
            mPlaylist = null;
        }

        //Get the list of existing mSongs
        if(mPlaylist.getSongList() == null){
            mChosenSongs = new ArrayList<>();
        }else {
            mChosenSongs = mPlaylist.getSongList();
        }

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        mSongsRecyclerView.setLayoutManager(layoutManager);
        songAdapter = new ChooseSongAdapter(this, mChosenSongs, getActivity());
        mSongsRecyclerView.setAdapter(songAdapter);

        setupSongAdapter();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AllMusicFragment.this.getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    private void setupSongAdapter(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mSongs = MusicUtils.getSongList(getActivity());

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(mSongs.size() > 0){
                            songAdapter.setSongs(mSongs);
                        } else {
                            mSongsRecyclerView.setVisibility(View.INVISIBLE);
                            mNoMusicTextView.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onClick(Song song) {
        if (this.mChosenSongs.contains(song)) {
            this.mChosenSongs.remove(song);
        } else {
            this.mChosenSongs.add(song);
        }
        if (!this.mShowSaveButton) {
            this.mSaveButton.show();
            this.mShowSaveButton = true;
        }
    }

    private void updatePlaylist(){
        mPlaylist.setMediaSource(mChosenSongs);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getActivity()).playlistDao().updatePlaylist(mPlaylist);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        updatePlaylist();
    }
}
