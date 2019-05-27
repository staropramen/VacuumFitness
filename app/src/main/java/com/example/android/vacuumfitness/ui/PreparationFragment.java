package com.example.android.vacuumfitness.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.utils.PreparationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreparationFragment extends Fragment {

    @BindView(R.id.iv_increase_button) ImageView increaseButton;
    @BindView(R.id.iv_decreasebutton) ImageView decreaseButton;
    @BindView(R.id.tv_exercise_count) TextView exerciseCount;

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

        //Setup Exercise count
        PreparationUtils.setExerciseCount(exerciseCount, increaseButton, decreaseButton);

        return rootView;
    }

}
