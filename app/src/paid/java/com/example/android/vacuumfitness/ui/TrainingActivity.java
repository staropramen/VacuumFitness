package com.example.android.vacuumfitness.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.android.vacuumfitness.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingActivity extends AppCompatActivity {

    private static String LOG_TAG = TrainingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        //Setup Butterknife
        ButterKnife.bind(this);

        //Handle when activity is recreated like on orientation Change
        if(savedInstanceState == null){
            PreparationFragment fragment = new PreparationFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.content_view, fragment)
                    .commit();
        }
    }
}
