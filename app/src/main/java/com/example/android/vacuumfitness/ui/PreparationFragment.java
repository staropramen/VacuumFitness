package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
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
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.utils.PreparationUtils;
import com.example.android.vacuumfitness.viewmodel.CustomTrainingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreparationFragment extends Fragment {

    List<Integer> primaryKeyList;
    int mTrainingPrimaryKey = -1;

    @BindView(R.id.iv_increase_button) ImageView increaseButton;
    @BindView(R.id.iv_decreasebutton) ImageView decreaseButton;
    @BindView(R.id.tv_exercise_count) TextView exerciseCount;
    @BindView(R.id.bt_start_training) Button startButton;
    @BindView(R.id.tv_time_count) TextView timeTextView;
    @BindView(R.id.sp_level_selection) Spinner levelSpinner;
    @BindView(R.id.sp_training_selection) Spinner trainingSpinner;

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

        //Setup Time
        PreparationUtils.calculateTime(exerciseCount, timeTextView, levelSpinner);

        //Setup Exercise count
        PreparationUtils.setExerciseCount(exerciseCount, increaseButton, decreaseButton, timeTextView, levelSpinner);

        //Listen to changes in levelSpinner and update time
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Set current PrimaryKey
                mTrainingPrimaryKey = primaryKeyList.get(i);

                PreparationUtils.calculateTime(exerciseCount, timeTextView, levelSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Setup Start Button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTrainingFragment();
            }
        });

        setupViewModel();

        return rootView;
    }

    private void startTrainingFragment(){

        Bundle data = new Bundle();
        data.putInt(KeyUtils.LEVEL_KEY, levelSpinner.getSelectedItemPosition());
        data.putInt(KeyUtils.EXERCISE_COUNT_KEY, Integer.parseInt(exerciseCount.getText().toString()));
        data.putString(KeyUtils.ID_LIST_KEY, ListConverter.fromList(primaryKeyList));

        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(data);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_view, fragment)
                .commit();
    }

    //Setup CustomTrainingViewModel
    private void setupViewModel(){
        CustomTrainingViewModel viewModel = ViewModelProviders.of(this).get(CustomTrainingViewModel.class);
        viewModel.getTrainings().observe(this, new Observer<List<Training>>() {
            @Override
            public void onChanged(@Nullable List<Training> trainings) {
                addSpinnerItems(trainings);
            }
        });
    }

    private void addSpinnerItems(List<Training> trainings){

        List<String> itemList = new ArrayList<>();
        itemList.add(getString(R.string.random_training));

        //Initialize primaryKeyList ans set -1 for Random Training
        primaryKeyList = new ArrayList<>();
        primaryKeyList.add(-1);

        // Add trainings to list
        for (int i = 0; i < trainings.size(); i++){
            Training currentTraining = trainings.get(i);
            String trainingName = currentTraining.getTrainingName();
            itemList.add(trainingName);
            primaryKeyList.add(currentTraining.getPrimaryKey());
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, itemList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        trainingSpinner.setAdapter(adapter);

    }

}
