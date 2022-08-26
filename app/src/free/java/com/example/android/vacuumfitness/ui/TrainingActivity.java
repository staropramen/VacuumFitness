package com.example.android.vacuumfitness.ui;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.utils.AdMobUtils;
import com.google.android.gms.ads.AdView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingActivity extends AppCompatActivity {

    private static String LOG_TAG = TrainingActivity.class.getSimpleName();

    @BindView(R.id.adView) AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        //Setup Butterknife
        ButterKnife.bind(this);

        //Load Ad banner
        AdMobUtils.loadAd(mAdView);

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
