package com.example.android.vacuumfitness.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @BindView(R.id.about_us_button) ImageView mAboutUsButton;
    @BindView(R.id.how_to_exercise) ImageView mHowToExerciseButton;
    @BindView(R.id.how_to_app) ImageView mHowToAppButton;

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
        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        toolbar.setTitle(getString(R.string.about));

        mAboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impressumFragmentTransaction();
            }
        });

        mHowToExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYoutube(getString(R.string.how_to_exercise_key));
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
