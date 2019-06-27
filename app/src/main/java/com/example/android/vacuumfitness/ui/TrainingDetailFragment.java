package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.ExerciseAdapter;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.viewmodel.ExerciseListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingDetailFragment extends Fragment implements ExerciseAdapter.ExerciseAdapterClickHandler{

    //TODO DELETE
    List<Integer> ids = new ArrayList<>();

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

        //Todo Delete
        ids.add(1);
        ids.add(2);
        ids.add(3);

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        exerciseRecyclerView.setLayoutManager(layoutManager);
        exerciseAdapter = new ExerciseAdapter(this);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        setupViewModel();

        return rootView;
    }

    private void setupViewModel() {
        ExerciseListViewModel popularViewModel = ViewModelProviders.of(this).get(ExerciseListViewModel.class);
        popularViewModel.getExercises(getActivity(), ids).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exerciseList) {
                if(exerciseList.isEmpty()){
                    //Show empty view
                    emptyListTextView.setVisibility(View.VISIBLE);
                    exerciseRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyListTextView.setVisibility(View.GONE);
                    exerciseRecyclerView.setVisibility(View.VISIBLE);
                    exerciseAdapter.setExercises(exerciseList);
                }
            }
        });
    }

    @Override
    public void onClick(Exercise exercise) {

    }
}
