package com.example.android.vacuumfitness.ui;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    boolean mSaveIsVisible;
    @BindView(R.id.rv_all_exercises) RecyclerView allExercisesRecyclerView;
    @BindView(R.id.fab_check) FloatingActionButton mSaveButton;

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

        //Hide Fab
        mSaveIsVisible = false;
        mSaveButton.hide();

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

        //Setup Fab Click
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AllExercisesFragment.this.getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    @Override
    public void onClick(Exercise exercise) {
        if (this.chosenExercises.contains(exercise)) {
            this.chosenExercises.remove(exercise);
        } else {
            this.chosenExercises.add(exercise);
        }
        if (!this.mSaveIsVisible) {
            this.mSaveButton.show();
            this.mSaveIsVisible = true;
        }
    }

    private void setupViewModel() {
        ExerciseListViewModel viewModel = new ViewModelProvider(this).get(ExerciseListViewModel.class);
        viewModel.getExercises().observe(getViewLifecycleOwner(), new Observer<List<Exercise>>() {
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
