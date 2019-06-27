package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.ExerciseAdapter;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.viewmodel.ExerciseListViewModel;
import com.example.android.vacuumfitness.viewmodel.SingleTrainingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingDetailFragment extends Fragment implements ExerciseAdapter.ExerciseAdapterClickHandler{

    private Toolbar toolbar;
    private long id;
    private List<Exercise> exercises;
    private LinearLayoutManager layoutManager;
    private ExerciseAdapter exerciseAdapter;
    @BindView(R.id.rv_exercises_by_ids) RecyclerView exerciseRecyclerView;
    @BindView(R.id.tv_empty_exercises) TextView emptyListTextView;


    public TrainingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_training_detail, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        exerciseRecyclerView.setLayoutManager(layoutManager);
        exerciseAdapter = new ExerciseAdapter(this);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.TRAINING_KEY)){
            id = data.getLong(KeyUtils.TRAINING_KEY);
        } else {
            id = -1;
        }

        //Set title and exercises count
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        setupExistingTraining();

        return rootView;
    }

    private void setupNewTraining() {
        exercises = new ArrayList<>();

    }

    private void setupExistingTraining() {
        SingleTrainingViewModel singleTrainingViewModel = ViewModelProviders.of(this).get(SingleTrainingViewModel.class);
        singleTrainingViewModel.getTraining(getActivity(), id).observe(this, new Observer<Training>() {
            @Override
            public void onChanged(@Nullable Training training) {
                //Set Title
                toolbar.setTitle(training.getTrainingName());

                exercises = training.getExerciseList();
                if(exercises == null|| exercises.isEmpty() ){
                    //Show empty view
                    emptyListTextView.setVisibility(View.VISIBLE);
                    exerciseRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyListTextView.setVisibility(View.GONE);
                    exerciseRecyclerView.setVisibility(View.VISIBLE);
                    exerciseAdapter.setExercises(exercises);
                }
            }
        });
    }

    @Override
    public void onClick(Exercise exercise) {

    }
}
