package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;
import com.example.android.vacuumfitness.utils.SpinnerUtils;
import com.example.android.vacuumfitness.viewmodel.CustomTrainingViewModel;
import com.example.android.vacuumfitness.viewmodel.PlaylistViewModel;
import com.example.android.vacuumfitness.viewmodel.TrainingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * I decided to use a normal Fragment for Settings instead of PreferenceFragment,
 * like this i can use Spinners, reuse code and design the Settings UI the way i want
 */
public class SettingsFragment extends Fragment {

    @BindView(R.id.sp_music_prefs) Spinner mMusicSpinner;
    @BindView(R.id.sp_training_prefs) Spinner mTrainingSpinner;
    @BindView(R.id.sp_level_prefs) Spinner mLevelSpinner;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        toolbar.setTitle(getString(R.string.settings));

        setupPlaylistViewModel();
        setupTrainingViewModel();

        //Set Level Spinner Position
        mLevelSpinner.setSelection(SharedPrefsUtils.getLevelSpinnerPosition());

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        //Save Music Spinner Position
        SharedPrefsUtils.saveMusicSpinnerPosition(mMusicSpinner.getSelectedItemPosition());

        //Save Training Spinner Position
        SharedPrefsUtils.saveTrainingSpinnerPosition(mTrainingSpinner.getSelectedItemPosition());

        //Save Level Spinner Position
        SharedPrefsUtils.saveLevelSpinnerPosition(mLevelSpinner.getSelectedItemPosition());
    }

    private void setupPlaylistViewModel(){
        PlaylistViewModel viewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        viewModel.getPlaylists().observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist> playlists) {
                SpinnerUtils.populateMusicSpinnerItems(getActivity(), mMusicSpinner, playlists);
                //Set Spinner on preferred Position
                mMusicSpinner.setSelection(SharedPrefsUtils.getMusicSpinnerPosition());
            }
        });
    }

    private void setupTrainingViewModel() {
        CustomTrainingViewModel viewModel = ViewModelProviders.of(this).get(CustomTrainingViewModel.class);
        viewModel.getTrainings().observe(this, new Observer<List<Training>>() {
            @Override
            public void onChanged(@Nullable List<Training> trainings) {
                SpinnerUtils.populateTrainingSpinnerItems(getActivity(), mTrainingSpinner, trainings);
                //Set Spinner on preferred Position
                mTrainingSpinner.setSelection(SharedPrefsUtils.getTrainingSpinnerPosition());
            }
        });
    }

}
