package com.example.android.vacuumfitness.ui;


import android.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private String mLabelString;
    @BindView(R.id.rv_trainings) RecyclerView trainingsRecyclerView;
    @BindView(R.id.tv_empty_trainings_list) TextView emptyListTextView;
    @BindView(R.id.bt_add_training) Button btAddTraining;

    private TextView mLabelTextView;
    private Training mTrainingToEdit;

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
        //showDeleteDialog(training);
        mTrainingToEdit = training;
        showAlertDialogButtonClicked(true);
    }

    //Setup CustomTrainingViewModel
    private void setupCustomTrainingViewModel(){
        CustomTrainingViewModel viewModel = new ViewModelProvider(this).get(CustomTrainingViewModel.class);
        viewModel.getTrainings().observe(getViewLifecycleOwner(), new Observer<List<Training>>() {
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
        btAddTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(false);
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

    private void updateTrainingInDb(final Training training){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getActivity()).trainingDao().updateTraining(training);

                trainingDetailFragmentTransaction(training.getPrimaryKey());
            }
        });
    }

    public void showAlertDialogButtonClicked(final boolean isLongClick) {

        // create an alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.cust_menu_title));

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.create_training_dialog, null);
        builder.setView(customLayout);

        //Get te EditTextViews
        final EditText nameET = customLayout.findViewById(R.id.et_custom_name);
        mLabelString = "";

        //Get the label views
        TextView tvMon = customLayout.findViewById(R.id.tv_label_mon);
        TextView tvTue = customLayout.findViewById(R.id.tv_label_tud);
        TextView tvWen = customLayout.findViewById(R.id.tv_label_wen);
        TextView tvThu = customLayout.findViewById(R.id.tv_label_thu);
        TextView tvFri = customLayout.findViewById(R.id.tv_label_fri);
        TextView tvSat = customLayout.findViewById(R.id.tv_label_sat);
        TextView tvSun = customLayout.findViewById(R.id.tv_label_sun);

        //Set variable to edit
        if(isLongClick){
            //Set visibility of delete icon
            ImageView deleteButton = customLayout.findViewById(R.id.iv_delete_icon);
            deleteButton.setVisibility(View.VISIBLE);
            //Set title
            nameET.setText(mTrainingToEdit.getTrainingName());
            //Get label
            String currentLabel = mTrainingToEdit.getLabel();
            mLabelString = currentLabel;
            //Set label color
            if(currentLabel.equals(getString(R.string.mon_label))){
                setTextViewBackgroundColor(tvMon, true);
                mLabelTextView = tvMon;
            } else if(currentLabel.equals(getString(R.string.tue_label))){
                setTextViewBackgroundColor(tvTue, true);
                mLabelTextView = tvTue;
            } else if(currentLabel.equals(getString(R.string.wed_label))){
                setTextViewBackgroundColor(tvWen, true);
                mLabelTextView = tvWen;
            } else if(currentLabel.equals(getString(R.string.thu_label))){
                setTextViewBackgroundColor(tvThu, true);
                mLabelTextView = tvThu;
            } else if(currentLabel.equals(getString(R.string.fri_label))){
                setTextViewBackgroundColor(tvFri, true);
                mLabelTextView = tvFri;
            } else if(currentLabel.equals(getString(R.string.sat_label))){
                setTextViewBackgroundColor(tvSat, true);
                mLabelTextView = tvSat;
            } else if(currentLabel.equals(getString(R.string.sun_label))){
                setTextViewBackgroundColor(tvSun, true);
                mLabelTextView = tvSun;
            }
        } else {
            mLabelTextView = null;
        }

        //Handle Click events for labels
        View.OnClickListener labelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there was a label choosed before, set the color back to light
                if(mLabelTextView != null){
                    setTextViewBackgroundColor(mLabelTextView, false);
                }
                mLabelTextView = (TextView)v;
                //Set Color of chosen label
                setTextViewBackgroundColor(mLabelTextView, true);
                mLabelString = mLabelTextView.getText().toString();
            }
        };

        tvMon.setOnClickListener(labelClickListener);
        tvTue.setOnClickListener(labelClickListener);
        tvWen.setOnClickListener(labelClickListener);
        tvThu.setOnClickListener(labelClickListener);
        tvFri.setOnClickListener(labelClickListener);
        tvSat.setOnClickListener(labelClickListener);
        tvSun.setOnClickListener(labelClickListener);

        // add a button
        builder.setPositiveButton(getString(R.string.positive_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Set back mLabelTextView to light color
                setTextViewBackgroundColor(mLabelTextView, false);

                String name = nameET.getText().toString();

                //Cancel if User don't entered a Training Name
                if(name.matches("")){
                    Toast.makeText(getActivity(), getString(R.string.no_name), Toast.LENGTH_LONG).show();
                }else if(isLongClick){
                    mTrainingToEdit.setTrainingName(name);
                    mTrainingToEdit.setLabel(mLabelString);
                    updateTrainingInDb(mTrainingToEdit);
                }else {
                    Training training = makeNewTraining(name, mLabelString);
                    saveNewTrainingInDb(training);
                }
            }
        });

        //add negative button
        builder.setNegativeButton(getString(R.string.negative_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Set back mLabelTextView to light color
                if(mLabelTextView != null){
                    setTextViewBackgroundColor(mLabelTextView, false);
                }
                dialog.cancel();
            }
        });

        // create the alert dialog
        final AlertDialog dialog = builder.create();

        //Set onclick action for delete icon
        if(isLongClick){
            //Setup delete Button
            customLayout.findViewById(R.id.iv_delete_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Set back mLabelTextView to light color
                    setTextViewBackgroundColor(mLabelTextView, false);

                    showDeleteDialog(mTrainingToEdit);
                    dialog.cancel();
                }
            });
        }

        //Show dialog
        dialog.show();
    }

    private void setTextViewBackgroundColor(TextView textView, boolean isColorDark){
        try {
            GradientDrawable background = (GradientDrawable) textView.getBackground();
            if(isColorDark){
                background.setColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                background.setColor(getResources().getColor(R.color.colorPrimaryLight));
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
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
