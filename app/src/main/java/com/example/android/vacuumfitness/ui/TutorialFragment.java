package com.example.android.vacuumfitness.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.android.vacuumfitness.R;

public class TutorialFragment extends Fragment {
    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_tutorial, viewGroup, false);
        ((Toolbar) getActivity().findViewById(R.id.toolbar))
                .setTitle(getString(R.string.how_to_exercise));
        return inflate;
    }
}
