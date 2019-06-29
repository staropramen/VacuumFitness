package com.example.android.vacuumfitness.ui;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.ExerciseAdapter;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.viewmodel.SingleTrainingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingDetailFragment extends Fragment implements ExerciseAdapter.ExerciseAdapterClickHandler, ExerciseAdapter.ExerciseAdapterLongClickHandler {

    private Toolbar toolbar;
    private long id;
    private Training mTraining;
    private List<Exercise> exercises;
    private LinearLayoutManager layoutManager;
    private ExerciseAdapter exerciseAdapter;
    @BindView(R.id.rv_exercises_by_ids) RecyclerView exerciseRecyclerView;
    @BindView(R.id.tv_empty_exercises) TextView emptyListTextView;
    @BindView(R.id.tv_exercises_count) TextView exercisesCountTextView;
    @BindView(R.id.fab_add_exercise) FloatingActionButton fabAddExercises;


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
        exerciseAdapter = new ExerciseAdapter(this, this);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.TRAINING_ID_KEY)){
            id = data.getLong(KeyUtils.TRAINING_ID_KEY);
        } else {
            id = -1;
        }

        //Set title and exercises count
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        //Setup Fab Button
        setupFabButton();

        setupExistingTraining();

        return rootView;
    }

    private void setupExistingTraining() {
        SingleTrainingViewModel singleTrainingViewModel = ViewModelProviders.of(this).get(SingleTrainingViewModel.class);
        singleTrainingViewModel.getTraining(getActivity(), id).observe(this, new Observer<Training>() {
            @Override
            public void onChanged(@Nullable Training training) {
                mTraining = training;

                //Set Title
                toolbar.setTitle(training.getTrainingName());

                exercises = training.getExerciseList();
                if(exercises == null|| exercises.isEmpty() ){
                    //Set exercises Count to zero
                    exercisesCountTextView.setText(String.valueOf(0));
                    //Show empty view
                    emptyListTextView.setVisibility(View.VISIBLE);
                    exerciseRecyclerView.setVisibility(View.GONE);
                } else {
                    //Set exercises count based on list size
                    exercisesCountTextView.setText(String.valueOf(exercises.size()));

                    emptyListTextView.setVisibility(View.GONE);
                    exerciseRecyclerView.setVisibility(View.VISIBLE);
                    exerciseAdapter.setExercises(exercises);
                }
            }
        });
    }

    @Override
    public void onClick(Exercise exercise) {
        //In this case we do nothing
    }

    @Override
    public void onLongClick(Exercise exercise) {
        showDeleteDialog(mTraining, exercise);
    }

    private void setupFabButton(){
        fabAddExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allExercisesFragmentTransaction();
            }
        });
    }

    private void allExercisesFragmentTransaction(){
        Bundle data = new Bundle();
        data.putParcelable(KeyUtils.TRAINING_KEY, mTraining);
        AllExercisesFragment fragment = new AllExercisesFragment();
        fragment.setArguments(data);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.customize_training_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showDeleteDialog(final Training training, final Exercise exercise){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.delete_exercise_title));
        alert.setMessage(getString(R.string.delete_question, exercise.getExerciseName()));
        alert.setPositiveButton(getString(R.string.delete_answer), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Exercise> exerciseList = training.getExerciseList();
                        exerciseList.remove(exercise);
                        training.setExerciseList(exerciseList);
                        AppDatabase.getInstance(getActivity()).trainingDao().updateTraining(training);
                    }
                });
            }
        });
        alert.setNegativeButton(getString(R.string.negative_answer), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });
        alert.show();
    }

}
