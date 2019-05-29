package com.example.android.vacuumfitness.ui;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.TrainingTimerUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {

    @BindView(R.id.tv_timer) TextView countdown;

    private int exerciseCount;
    private int level;

    MediaPlayer mCommandMediaPlayer;

    public TrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_training, container, false);

        //Setup Butteknife
        ButterKnife.bind(this, rootView);

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.EXERCISE_COUNT_KEY) && data.containsKey(KeyUtils.LEVEL_KEY)){
            exerciseCount = data.getInt(KeyUtils.EXERCISE_COUNT_KEY);
            level = data.getInt(KeyUtils.LEVEL_KEY);
        }

        //Launch Countdown
        getCountdown(TrainingTimerUtils.getTrainingTimeMilliseconds(level, exerciseCount));

        return rootView;
    }

    private void getCountdown(long time){
        new CountDownTimer(time, 1000) {

            int counter = 0;

            public void onTick(long millisUntilFinished) {
                countdown.setText(""+String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                int voiceCommand = TrainingTimerUtils.getVoiceCommandInt(TrainingTimerUtils.getCommandCorners(level), counter, getActivity());
                playVoiceCommand(voiceCommand);

                if(counter < TrainingTimerUtils.exerciseTime(level)){
                    counter++;
                }else {
                    counter = 0;
                }
            }

            public void onFinish() {
                countdown.setText("done!");
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    private void playVoiceCommand(int audioId){
        //If there is no commant audioId is 0, so dont launch Mediaplayer then
        if(audioId != 0){
            releaseMediaPlayer();
            mCommandMediaPlayer = MediaPlayer.create(getActivity(), audioId);
            mCommandMediaPlayer.start();
        }
    }


    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mCommandMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mCommandMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mCommandMediaPlayer = null;
        }
    }
}


