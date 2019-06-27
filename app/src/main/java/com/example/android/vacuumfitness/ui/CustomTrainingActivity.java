package com.example.android.vacuumfitness.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Training;

public class CustomTrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_training);

        customTrainingFragmentTransaction();
    }

    private void customTrainingFragmentTransaction(){
        CustomizeTrainingFragment fragment = new CustomizeTrainingFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.customize_training_content, fragment)
                .commit();
    }
}

