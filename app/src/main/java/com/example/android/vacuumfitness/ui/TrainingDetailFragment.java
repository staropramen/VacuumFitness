package com.example.android.vacuumfitness.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.vacuumfitness.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingDetailFragment extends Fragment {


    public TrainingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_training_detail, container, false);

        return rootView;
    }

}
