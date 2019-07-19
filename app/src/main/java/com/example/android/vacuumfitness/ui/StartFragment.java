package com.example.android.vacuumfitness.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.service.UpdateMotivatorsService;
import com.example.android.vacuumfitness.service.UpdateMotivatorsServiceOld;
import com.example.android.vacuumfitness.service.UpdateMotivatorsTask;
import com.example.android.vacuumfitness.utils.AppExecutors;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    @BindView(R.id.start_training_button) ImageView startTraining;
    @BindView(R.id.customize_training_button) ImageView customizeTraining;
    @BindView(R.id.customize_music_button) ImageView customizeMusic;
    @BindView(R.id.how_to_button) ImageView howToButton;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        toolbar.setTitle(getString(R.string.app_name));


        //Set onClick for StartTraining
        startTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrainingActivity.class);
                startActivity(intent);
            }
        });

        //Setup onClick for CustomizeTraining
        customizeTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomTrainingActivity.class);
                startActivity(intent);
            }
        });

        //Setup onClick for CustomizeMusic
        customizeMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomizeMusicActivity.class);
                startActivity(intent);
            }
        });

        //Setup Hot To Button
        howToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Motivator motivator = AppDatabase.getInstance(getActivity()).motivatorDao().loadMotivatorById(1);
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                String motivatorText = motivator.getMotivationText();
                                Toast.makeText(getActivity(), motivatorText, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        return rootView;
    }

}
