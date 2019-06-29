package com.example.android.vacuumfitness.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.vacuumfitness.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    @BindView(R.id.start_training_button) ImageView startTraining;
    @BindView(R.id.customize_training_button) ImageView customizeTraining;
    @BindView(R.id.customize_music_button) ImageView customizeMusic;


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

        return rootView;
    }

}
