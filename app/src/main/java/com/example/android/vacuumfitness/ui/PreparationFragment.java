package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.IdListUtils;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.utils.PreparationUtils;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;
import com.example.android.vacuumfitness.utils.SpinnerUtils;
import com.example.android.vacuumfitness.viewmodel.CustomTrainingViewModel;
import com.example.android.vacuumfitness.viewmodel.PlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreparationFragment extends Fragment {

    private List<Training> mTrainingList;
    private List<Playlist> mPlaylistList;
    private int mRandomTrainingPrimaryKey = -1;
    private int mEmptyPlaylistPrimaryKey = -1;
    private List<Integer> mPrimaryKeyList;
    private Training mTraining;
    private Playlist mPlaylist;

    @BindView(R.id.iv_increase_button) ImageView increaseButton;
    @BindView(R.id.iv_decreasebutton) ImageView decreaseButton;
    @BindView(R.id.tv_exercise_count) TextView exerciseCount;
    @BindView(R.id.bt_start_training) Button startButton;
    @BindView(R.id.tv_time_count) TextView timeTextView;
    @BindView(R.id.sp_level_selection) Spinner levelSpinner;
    @BindView(R.id.sp_training_selection) Spinner trainingSpinner;
    @BindView(R.id.sp_music_selection) Spinner musicSpinner;

    public PreparationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_preparation, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //By default mTraining is a RandomTraining with primaryKey -1
        mTraining = new Training();
        mTraining.setPrimaryKey(mRandomTrainingPrimaryKey);
        //Initialize primaryKeyList ans set -1 for Random Training
        mTrainingList = new ArrayList<>();
        mTrainingList.add(mTraining);

        //By default mPlaylist is a emty plylist with primary key -1
        mPlaylist = new Playlist();
        mPlaylist.setPrimaryKey(mEmptyPlaylistPrimaryKey);

        //Setup Time
        PreparationUtils.calculateTime(exerciseCount, timeTextView, levelSpinner);

        //Setup Start Button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTrainingFragment();
            }
        });

        setupTrainingViewModel();
        setupPlaylistViewModel();

        //Set Level Spinner Position
        levelSpinner.setSelection(SharedPrefsUtils.getLevelSpinnerPosition());

        setupSpinnerOnSelectListener();

        return rootView;
    }

    private void startTrainingFragment(){

        Bundle data = new Bundle();
        data.putInt(KeyUtils.LEVEL_KEY, levelSpinner.getSelectedItemPosition());
        data.putInt(KeyUtils.EXERCISE_COUNT_KEY, Integer.parseInt(exerciseCount.getText().toString()));
        mPrimaryKeyList = IdListUtils.getTrainingIdList(mTraining, Integer.parseInt(exerciseCount.getText().toString()));
        data.putString(KeyUtils.ID_LIST_KEY, ListConverter.fromList(mPrimaryKeyList));
        data.putParcelable(KeyUtils.PLAYLIST_KEY, mPlaylist);

        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(data);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_view, fragment)
                .commit();
    }

    //Setup CustomTrainingViewModel
    private void setupTrainingViewModel(){
        CustomTrainingViewModel viewModel = ViewModelProviders.of(this).get(CustomTrainingViewModel.class);
        viewModel.getTrainings().observe(this, new Observer<List<Training>>() {
            @Override
            public void onChanged(@Nullable List<Training> trainings) {
                mTrainingList = SpinnerUtils.populateTrainingSpinnerItems(getActivity(), trainingSpinner, trainings);

                //Set Spinner on preferred Position after check if is still existing
                int trainingSelection = SharedPrefsUtils.getTrainingSpinnerPosition();

                if(mTrainingList.size() > trainingSelection){
                    Log.d("!!!!!", "IF Training");
                    trainingSpinner.setSelection(trainingSelection);
                }
            }
        });
    }

    private void setupPlaylistViewModel(){
        PlaylistViewModel viewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        viewModel.getPlaylists().observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist> playlists) {
                mPlaylistList = SpinnerUtils.populateMusicSpinnerItems(getActivity(), musicSpinner, playlists);
                //Set Spinner on preferred Position after check if is still existing
                int playlistSelection = SharedPrefsUtils.getMusicSpinnerPosition();

                Log.d("!!!!!", String.valueOf(playlistSelection));
                Log.d("!!!!!", String.valueOf(playlists.size()));

                if(mPlaylistList.size() > playlistSelection){
                    Log.d("!!!!!", "IF Playlist");
                    musicSpinner.setSelection(playlistSelection);
                }
            }
        });
    }

    private void setupSpinnerOnSelectListener(){
        //Listen to changes in levelSpinner and update time
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PreparationUtils.calculateTime(exerciseCount, timeTextView, levelSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        trainingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Set current Training
                mTraining = mTrainingList.get(position);

                //Set exerciseCount Based on training
                exerciseCount.setText(String.valueOf(
                        PreparationUtils.getExerciseCount(mTraining)
                ));
                //Setup Exercise count
                PreparationUtils.setExerciseCount(exerciseCount, increaseButton, decreaseButton, timeTextView, levelSpinner, mTraining);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        musicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPlaylist = mPlaylistList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
