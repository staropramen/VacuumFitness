package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.ChooseExerciseAdapter;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.viewmodel.ExerciseListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllExercisesFragment extends Fragment implements ChooseExerciseAdapter.ChooseExerciseClickHandler {

    private List<Exercise> chosenExercises;
    private Training mTraining;
    private LinearLayoutManager layoutManager;
    private ChooseExerciseAdapter exerciseAdapter;
    @BindView(R.id.rv_all_exercises) RecyclerView allExercisesRecyclerView;

    public AllExercisesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_exercises, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.all_exercises);

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.TRAINING_KEY)){
            mTraining = data.getParcelable(KeyUtils.TRAINING_KEY);
        } else {
            mTraining = null;
        }

        //Get the list of existing exercises
        if(mTraining.getExerciseList() == null){
            chosenExercises = new ArrayList<>();
        }else {
            chosenExercises = mTraining.getExerciseList();
        }

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        allExercisesRecyclerView.setLayoutManager(layoutManager);
        exerciseAdapter = new ChooseExerciseAdapter(this, mTraining.getExerciseList(), getActivity());
        allExercisesRecyclerView.setAdapter(exerciseAdapter);

        setupViewModel();

        return rootView;
    }

    @Override
    public void onClick(Exercise exercise) {
        if(chosenExercises.contains(exercise)){
            chosenExercises.remove(exercise);
        } else {
            chosenExercises.add(exercise);
        }

    }

    private void setupViewModel() {
        ExerciseListViewModel viewModel = ViewModelProviders.of(this).get(ExerciseListViewModel.class);
        viewModel.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exerciseList) {
                exerciseAdapter.setExercises(exerciseList);
            }
        });
    }

    private void updateTraining() {
        mTraining.setExerciseList(chosenExercises);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getActivity()).trainingDao().updateTraining(mTraining);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        updateTraining();
    }
}
