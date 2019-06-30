package com.example.android.vacuumfitness.ui;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.adapter.CustomTrainingAdapter;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Training;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.viewmodel.CustomTrainingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomizeTrainingFragment extends Fragment implements CustomTrainingAdapter.CustomTrainingOnClickHandler, CustomTrainingAdapter.CustomTrainingOnLongClickHandler {

    private LinearLayoutManager layoutManager;
    private CustomTrainingAdapter trainingAdapter;
    @BindView(R.id.rv_trainings) RecyclerView trainingsRecyclerView;
    @BindView(R.id.tv_empty_trainings_list) TextView emptyListTextView;
    @BindView(R.id.fab_add_training) FloatingActionButton fabAddTraining;

    private TextView labelTextView = null;

    public CustomizeTrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customize_training, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.customize_training_title);

        //Prepare RecyclerView
        layoutManager = new LinearLayoutManager(getContext());
        trainingsRecyclerView.setLayoutManager(layoutManager);
        trainingAdapter = new CustomTrainingAdapter(this, this);
        trainingsRecyclerView.setAdapter(trainingAdapter);

        setupCustomTrainingViewModel();

        setupFabButton();

        return rootView;
    }

    @Override
    public void onClick(Training training) {
        long id = training.getPrimaryKey();
        trainingDetailFragmentTransaction(id);
    }

    @Override
    public void onLongClick(Training training) {
        showDeleteDialog(training);
    }

    //Setup CustomTrainingViewModel
    private void setupCustomTrainingViewModel(){
        CustomTrainingViewModel viewModel = ViewModelProviders.of(this).get(CustomTrainingViewModel.class);
        viewModel.getTrainings().observe(this, new Observer<List<Training>>() {
            @Override
            public void onChanged(@Nullable List<Training> trainings) {
                if(trainings.isEmpty()){
                    //Show empty view
                    emptyListTextView.setVisibility(View.VISIBLE);
                    trainingsRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyListTextView.setVisibility(View.GONE);
                    trainingsRecyclerView.setVisibility(View.VISIBLE);
                    trainingAdapter.setTrainings(trainings);
                }
            }
        });
    }

    //Setup Fab button
    private void setupFabButton(){
        fabAddTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked();
            }
        });
    }

    private void trainingDetailFragmentTransaction(long id) {
        Bundle data = new Bundle();
        data.putLong(KeyUtils.TRAINING_ID_KEY, id);
        TrainingDetailFragment fragment = new TrainingDetailFragment();
        fragment.setArguments(data);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.customize_training_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Training makeNewTraining(String name, String label){
        List<Exercise> list = new ArrayList<>();
        Training training = new Training(name, label, list);
        return training;
    }

    private void saveNewTrainingInDb(final Training training) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Long id = AppDatabase.getInstance(getActivity()).trainingDao().insertTraining(training);

                trainingDetailFragmentTransaction(id);
            }
        });
    }

    public void showAlertDialogButtonClicked() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.cust_menu_title));

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.create_training_dialog, null);
        builder.setView(customLayout);

        //Handle Click events for labels
        View.OnClickListener labelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there was a laber choosed before, set the color back to light
                if(labelTextView != null){
                    GradientDrawable background = (GradientDrawable) labelTextView.getBackground();
                    background.setColor(getResources().getColor(R.color.colorPrimaryLight));
                }
                labelTextView = (TextView)v;
                //Set Color of chosen label
                GradientDrawable background = (GradientDrawable) labelTextView.getBackground();
                background.setColor(getResources().getColor(R.color.colorPrimaryDark));
                String label = labelTextView.getText().toString();
                EditText editText = customLayout.findViewById(R.id.et_custom_label);
                editText.setText(label);
            }
        };

        customLayout.findViewById(R.id.tv_label_mon).setOnClickListener(labelClickListener);
        customLayout.findViewById(R.id.tv_label_tud).setOnClickListener(labelClickListener);
        customLayout.findViewById(R.id.tv_label_wen).setOnClickListener(labelClickListener);
        customLayout.findViewById(R.id.tv_label_thu).setOnClickListener(labelClickListener);
        customLayout.findViewById(R.id.tv_label_fri).setOnClickListener(labelClickListener);
        customLayout.findViewById(R.id.tv_label_sat).setOnClickListener(labelClickListener);
        customLayout.findViewById(R.id.tv_label_sun).setOnClickListener(labelClickListener);

        // add a button
        builder.setPositiveButton(getString(R.string.positive_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText nameET = customLayout.findViewById(R.id.et_custom_name);
                EditText labelET = customLayout.findViewById(R.id.et_custom_label);

                String name = nameET.getText().toString();
                String label = labelET.getText().toString();

                //Cancel if User dont entern a Training Name
                if(name.matches("")){
                    Toast.makeText(getActivity(), getString(R.string.no_name), Toast.LENGTH_LONG).show();
                }else {
                    Training training = makeNewTraining(name, label);
                    saveNewTrainingInDb(training);
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteDialog(final Training training){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.delete_training_title));
        alert.setMessage(getString(R.string.delete_question, training.getTrainingName()));
        alert.setPositiveButton(getString(R.string.delete_answer), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(getActivity()).trainingDao().deleteTraining(training);
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
