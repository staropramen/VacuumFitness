package com.example.android.vacuumfitness.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.utils.TrainingTimerUtils;
import com.example.android.vacuumfitness.viewmodel.TrainingViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {

    private static String LOG_TAG = TrainingFragment.class.getSimpleName();

    @BindView(R.id.tv_timer) TextView mCountdown;
    @BindView(R.id.tv_exercise_count_of) TextView mExerciseCount;
    @BindView(R.id.tv_exercise_name) TextView mExerciseName;
    @BindView(R.id.iv_exercise_image) ImageView mExerciseImage;

    private int exerciseCount;
    private int level;

    private int mTimeCounter = 0;
    private int mExercisePosition = 1;
    private List<Exercise> mExerciseList;
    private long mTrainingTime;
    private CountDownTimer mCountDownTimer;

    private MediaPlayer mCommandMediaPlayer;

    private AppDatabase mDb;

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

        //Get Database instance
        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.EXERCISE_COUNT_KEY) && data.containsKey(KeyUtils.LEVEL_KEY)){
            exerciseCount = data.getInt(KeyUtils.EXERCISE_COUNT_KEY);
            level = data.getInt(KeyUtils.LEVEL_KEY);
        }

        //Make the Training time
        mTrainingTime = TrainingTimerUtils.getTrainingTimeMilliseconds(level, exerciseCount);
        Log.d(LOG_TAG, String.valueOf(mTrainingTime));

        //Launch ViewModel if savedInstanceState is null
        if(savedInstanceState == null){
            setupRandomTrainingViewModel();
        } else {
            //Set back state before saving
            mTrainingTime = savedInstanceState.getLong(KeyUtils.TRAINING_TIME);
            mTimeCounter = savedInstanceState.getInt(KeyUtils.EXERCISE_TIME);
            mExerciseList = ListConverter.stringToExerciseList(savedInstanceState.getString(KeyUtils.EXERCISES_ARRAY));
            mExercisePosition = savedInstanceState.getInt(KeyUtils.EXERCISE_POSITION);
            setupRandomTrainingViewModel();
        }
        return rootView;
    }

    private void getCountdown(long time, final List<Exercise> exerciseList){

        mCountDownTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                //Keep Training Time Up to Date
                mTrainingTime = millisUntilFinished;

                mCountdown.setText(""+String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                int voiceCommand = TrainingTimerUtils.getVoiceCommandInt(TrainingTimerUtils.getCommandCorners(level), mTimeCounter, getActivity());
                playVoiceCommand(voiceCommand);


                if (mTimeCounter < TrainingTimerUtils.exerciseTime(level)){
                    mTimeCounter++;
                } else {
                    mTimeCounter = 0;
                    mExercisePosition++;
                    populateUi(mExercisePosition, exerciseList);
                }
            }

            public void onFinish() {
                mCountdown.setText("done!");
            }
        }.start();
    }

    private void stopCountdown(){
        mCountDownTimer.cancel();
    }

    //Setup ViewModel
    private void setupRandomTrainingViewModel(){
        TrainingViewModel trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        trainingViewModel.getExercises(getActivity(), exerciseCount).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                //Initially populate ui
                populateUi(mExercisePosition, exercises);
                //Launch Countdown
                mExerciseList = exercises;
                getCountdown(mTrainingTime, mExerciseList);
            }
        });
    }

    private void populateUi(int counter, List<Exercise> exerciseList){
        //Get current exercise
        Exercise currentExercise = exerciseList.get(counter - 1);
        //Set Exercise count
        mExerciseCount.setText(TrainingTimerUtils.makeExerciseCountString(counter, exerciseCount));
        //Set exercise name
        mExerciseName.setText(currentExercise.getExerciseName());
        //Set exercise image
        String imagePath = currentExercise.getImage();
        int resId = TrainingTimerUtils.getResId(imagePath, R.drawable.class);
        if (resId != -1) {
            Picasso.get().load(resId).into(mExerciseImage);
        } else {
            //If path not found load a dummy picture
            Picasso.get().load(R.drawable.dummy1).into(mExerciseImage);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
        stopCountdown();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KeyUtils.EXERCISE_POSITION, mExercisePosition);
        outState.putInt(KeyUtils.EXERCISE_TIME, mTimeCounter);
        outState.putString(KeyUtils.EXERCISES_ARRAY, ListConverter.exerciseListToString(mExerciseList));
        outState.putLong(KeyUtils.TRAINING_TIME, mTrainingTime);
        super.onSaveInstanceState(outState);

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

    private void getRandomExercisesArray(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int exerciseTableRows = mDb.exerciseDao().getExerciseRowCount();
            }
        });
    }
}


