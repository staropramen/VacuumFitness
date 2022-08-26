package com.example.android.vacuumfitness.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    @BindView(R.id.about_us_button) Button mAboutUsButton;
    @BindView(R.id.how_to_exercise) Button mHowToExerciseButton;
    @BindView(R.id.how_to_app) Button mHowToAppButton;
    @BindView(R.id.how_to_exercise_video) Button mHowToExerciseVideoButton;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        //Setup Butterknife
        ButterKnife.bind(this, rootView);

        //Set the title
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(getString(R.string.about));

        mAboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impressumFragmentTransaction();
            }
        });

        mHowToExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutFragment.this.tutorialFragmentTransaction();
            }
        });

        mHowToExerciseVideoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AboutFragment aboutFragment = AboutFragment.this;
                aboutFragment.openYoutube(aboutFragment.getString(R.string.how_to_exercise_key));
            }
        });

        mHowToAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYoutube(getString(R.string.how_to_app_key));
            }
        });

        return rootView;
    }

    private void impressumFragmentTransaction() {
        ImpressumFragment fragment = new ImpressumFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.start_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void tutorialFragmentTransaction() {
        TutorialFragment tutorialFragment = new TutorialFragment();
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.start_content, tutorialFragment);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();
    }

    private void openYoutube(String youtubeKey) {
        //Check for Internet Connection
        if(NetworkUtils.isConnectedToInternet()){
            //Check for video key
            if(youtubeKey != null && !TextUtils.isEmpty(youtubeKey)){
                String webUrl = getString(R.string.youtube_web_url )+ youtubeKey;
                String appUrl = getString(R.string.youtube_app_url )+ youtubeKey;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(webUrl));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }else {
                Toast.makeText(getActivity(), getString(R.string.youtube_error), Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

}
