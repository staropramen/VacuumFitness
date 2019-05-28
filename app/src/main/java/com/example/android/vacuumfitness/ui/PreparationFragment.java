package com.example.android.vacuumfitness.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.utils.KeyUtils;
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
    @BindView(R.id.bt_start_training) Button startButton;
    @BindView(R.id.tv_time_count) TextView timeTextView;
    @BindView(R.id.sp_level_selection) Spinner levelSpinner;

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

        //Setup Time
        PreparationUtils.calculateTime(exerciseCount, timeTextView, levelSpinner);

        //Setup Exercise count
        PreparationUtils.setExerciseCount(exerciseCount, increaseButton, decreaseButton, timeTextView, levelSpinner);

        //Listen to changes in levelSpinner and update time
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PreparationUtils.calculateTime(exerciseCount, timeTextView, levelSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Setup Start Button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTrainingFragment();
            }
        });

        return rootView;
    }

    private void startTrainingFragment(){

        Bundle data = new Bundle();
        data.putInt(KeyUtils.LEVEL_KEY, levelSpinner.getSelectedItemPosition());
        data.putInt(KeyUtils.EXERCISE_COUNT_KEY, Integer.parseInt(exerciseCount.getText().toString()));

        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(data);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_view, fragment)
                .commit();
    }

}
