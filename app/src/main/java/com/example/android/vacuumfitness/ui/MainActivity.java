package com.example.android.vacuumfitness.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;

    public static SharedPreferences sharedPreferences;
    public static Context mContext;

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

        //Do things on First Run
        doOnFirstRun();

        //Setup Toolbar
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

    }

    //A Function to do things only at first run of the App
    private void doOnFirstRun(){
        if(SharedPrefsUtils.getIsFirstRun()){
            Log.d(LOG_TAG, "Fist Run Function Executed");
            //If i populate Database in onCreateDatabase Appi is crashing because Data on first Run are not available
            //Im calling here a Function to count the rows, only that onCreateDb gets triggered
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance(getApplicationContext()).exerciseDao().getExerciseRowCount();
                }
            });

            //In the end i save a boolean to SharedPrefs that App knows that is not the first Run anymore
            SharedPrefsUtils.saveIsFirstRunToPrefs();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

