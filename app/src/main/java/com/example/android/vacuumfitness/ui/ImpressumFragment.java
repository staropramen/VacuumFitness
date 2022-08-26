package com.example.android.vacuumfitness.ui;


import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.vacuumfitness.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImpressumFragment extends Fragment {

    public ImpressumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_impressum, container, false);

        //Set the title
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(getString(R.string.about_us));

        return rootView;
    }
}
