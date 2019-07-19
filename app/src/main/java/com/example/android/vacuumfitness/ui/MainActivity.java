package com.example.android.vacuumfitness.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;
import com.example.android.vacuumfitness.worker.UpdateMotivatorWorker;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    private static String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

    public static SharedPreferences sharedPreferences;
    public static Context mContext;
    public static String mDefaultMotivationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Butterknife
        ButterKnife.bind(this);

        //Get shared preferences to use in non-activity class
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        //Get ApplicationContext to use in non-activity class
        mContext = getApplicationContext();
        mDefaultMotivationText = getString(R.string.default_motivator_string);

        //Do things on First Run
        doOnFirstRun();

        //Setup Toolbar
        setSupportActionBar(toolbar);


        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        //Handle when activity is recreated like on orientation Change
        if(savedInstanceState == null){
            StartFragment fragment = new StartFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.start_content, fragment)
                    .commit();
        }

        //Activity is ready now we check for Widget Intent
        checkForWidgetIntent();

    }

    //A Function to do things only at first run of the App
    private void doOnFirstRun(){
        if(SharedPrefsUtils.getIsFirstRun(this)){
            Log.d(LOG_TAG, "Fist Run Function Executed");
            //If i populate Database in onCreateDatabase Appi is crashing because Data on first Run are not available
            //Im calling here a Function to count the rows, that onCreateDb gets triggered and rows for Service will be saved in shared prefs
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance(getApplicationContext()).motivatorDao().getMotivatorsRowCount();
                }
            });

            //Set tasks for WorkManager
            setupWorker();

            //In the end i save a boolean to SharedPrefs that App knows that is not the first Run anymore
            SharedPrefsUtils.saveIsFirstRunToPrefs(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.settings:
                settingsFragmentTransaction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settingsFragmentTransaction() {
        SettingsFragment fragment = new SettingsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.start_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void checkForWidgetIntent() {
        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(KeyUtils.WIDGET_INTENT)) {
            //Open Training Activity
            Intent i = new Intent(this, TrainingActivity.class);
            startActivity(i);
        }
    }

    private void setupWorker() {
        //Setup the constraints for workmanager
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        //Setup the periodic work for 1 week
        PeriodicWorkRequest updateMotivatorsTask =
                new PeriodicWorkRequest.Builder(UpdateMotivatorWorker.class, 7, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();

        //Setup Workmanager
        WorkManager.getInstance()
                .enqueue(updateMotivatorsTask);
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 0){
            finish();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 0){
            finish();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}

