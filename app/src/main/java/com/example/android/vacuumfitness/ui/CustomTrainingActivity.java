package com.example.android.vacuumfitness.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Training;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomTrainingActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_training);

        //Setup Butterknife
        ButterKnife.bind(this);

        //Setup Toolbar for BackButton
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        //Handle when activity is recreated like on orientation Change
        if(savedInstanceState == null){
            CustomizeTrainingFragment fragment = new CustomizeTrainingFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.customize_training_content, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    //Fragment Navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}

