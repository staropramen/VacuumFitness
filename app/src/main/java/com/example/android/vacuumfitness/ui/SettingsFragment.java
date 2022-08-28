package com.example.android.vacuumfitness.ui;


import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;
import com.example.android.vacuumfitness.utils.SpinnerUtils;
import com.example.android.vacuumfitness.viewmodel.CustomTrainingViewModel;
import com.example.android.vacuumfitness.viewmodel.PlaylistViewModel;

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
    @BindView(R.id.cb_duck_music) CheckBox mDuckMusicCheckBox;
    @BindView(R.id.toggle_voice) Switch mVoiceToggle;
    @BindView(R.id.toggle_visual) Switch mVisualToggle;
    @BindView(R.id.cb_prefer_day) CheckBox mPreferCheckBox;

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
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(getString(R.string.settings));

        setupPlaylistViewModel();
        setupTrainingViewModel();

        //Set Level Spinner Position
        mLevelSpinner.setSelection(SharedPrefsUtils.getLevelSpinnerPosition(getActivity()));

        //Set Duck Music CheckBox
        mDuckMusicCheckBox.setChecked(SharedPrefsUtils.getDuckMusicBoolean(getActivity()));

        //Set prefered Training Spinner
        mPreferCheckBox.setChecked(SharedPrefsUtils.getPreferDayBoolean(getActivity()));


        //Set voice and visual toggle
        mVoiceToggle.setChecked(SharedPrefsUtils.getVoiceToggleBoolean(getActivity()));
        mVisualToggle.setChecked(SharedPrefsUtils.getVisualToggleBoolean(getActivity()));
        controlSwitchBehaviour(mVoiceToggle, mVisualToggle);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        //Save Music Spinner Position
        SharedPrefsUtils.saveMusicSpinnerPosition(getActivity(), mMusicSpinner.getSelectedItemPosition());

        //Save Training Spinner Position
        SharedPrefsUtils.saveTrainingSpinnerPosition(getActivity(), mTrainingSpinner.getSelectedItemPosition());

        //Save Level Spinner Position
        SharedPrefsUtils.saveLevelSpinnerPosition(getActivity(), mLevelSpinner.getSelectedItemPosition());

        //Save Duck Music Boolean
        boolean isChecked = mDuckMusicCheckBox.isChecked();
        SharedPrefsUtils.saveDuckMusicBoolean(getActivity(), isChecked);

        //Save voice and visual toggle booleans
        SharedPrefsUtils.saveVoiceToggleBoolean(getActivity(), mVoiceToggle.isChecked());
        SharedPrefsUtils.saveVisualToggleBoolean(getActivity(), mVisualToggle.isChecked());

    }

    private void setupPlaylistViewModel(){
        PlaylistViewModel viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        viewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist> playlists) {
                SpinnerUtils.populateMusicSpinnerItems(getActivity(), mMusicSpinner, playlists);
                //Set Spinner on preferred Position
                mMusicSpinner.setSelection(SharedPrefsUtils.getMusicSpinnerPosition(getActivity()));
            }
        });
    }

    private void setupTrainingViewModel() {
        CustomTrainingViewModel viewModel = new ViewModelProvider(this).get(CustomTrainingViewModel.class);
        viewModel.getTrainings().observe(getViewLifecycleOwner(), new Observer<List<Training>>() {
            @Override
            public void onChanged(@Nullable List<Training> trainings) {
                SpinnerUtils.populateTrainingSpinnerItems(getActivity(), mTrainingSpinner, trainings);
                //Set Spinner on preferred Position
                mTrainingSpinner.setSelection(SharedPrefsUtils.getTrainingSpinnerPosition(getActivity()));
            }
        });
    }

    //Control switch behaviour that its not possible to have both switches off
    private void controlSwitchBehaviour(final Switch voiceSwitch, final Switch visualSwitch){
        voiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked && !visualSwitch.isChecked()){
                    visualSwitch.setChecked(true);
                }
            }
        });

        visualSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked && !voiceSwitch.isChecked()){
                    voiceSwitch.setChecked(true);
                }
            }
        });
    }

}
