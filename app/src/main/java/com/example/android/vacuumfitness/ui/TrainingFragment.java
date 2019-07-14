package com.example.android.vacuumfitness.ui;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Playlist;
import com.example.android.vacuumfitness.model.Song;
import com.example.android.vacuumfitness.utils.ExoPlayerUtils;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.utils.MusicUtils;
import com.example.android.vacuumfitness.utils.NetworkUtils;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;
import com.example.android.vacuumfitness.utils.TrainingTimerUtils;
import com.example.android.vacuumfitness.viewmodel.TrainingViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment implements Player.EventListener {

    private static String LOG_TAG = TrainingFragment.class.getSimpleName();

    @BindView(R.id.tv_timer) TextView mCountdown;
    @BindView(R.id.tv_exercise_count_of) TextView mExerciseCount;
    @BindView(R.id.tv_exercise_list_name) TextView mExerciseName;
    @BindView(R.id.iv_exercise_image) ImageView mExerciseImage;
    @BindView(R.id.iv_start_pause) ImageView mStartPauseIV;
    @BindView(R.id.iv_video_button) ImageView mVideoButton;
    @BindView(R.id.iv_music_button) ImageView mMusicButton;

    private int exerciseCount;
    private int level;

    private int mTimeCounter = 0;
    private int mExercisePosition = 1;
    private List<Exercise> mExerciseList;
    private long mTrainingTime;
    private CountDownTimer mCountDownTimer;
    private boolean mTrainingIsPaused;
    private List<Integer> mIdList;

    private MediaPlayer mCommandMediaPlayer;

    private String EXO_VIDEO_PLAYER = "exoVideoPlayer";
    private ConcatenatingMediaSource mMediaSource;
    private Playlist mPlaylist;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayer mExoPlayer;
    private boolean mHasMusic = false;

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

        //Let activity not sleep if this fragment is alive
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.EXERCISE_COUNT_KEY) && data.containsKey(KeyUtils.LEVEL_KEY)){
            exerciseCount = data.getInt(KeyUtils.EXERCISE_COUNT_KEY);
            level = data.getInt(KeyUtils.LEVEL_KEY);
            mIdList = ListConverter.fromString(data.getString(KeyUtils.ID_LIST_KEY));
            mPlaylist = data.getParcelable(KeyUtils.PLAYLIST_KEY);
        }

        //Make the Training time
        mTrainingTime = TrainingTimerUtils.getTrainingTimeMilliseconds(level, exerciseCount);

        //Prepare the Media Source for Exoplayer
        if(mPlaylist != null){
            //Playlist is no null, now we check if these is a Songlist with songs inside
            if(mPlaylist.getSongList() != null && !mPlaylist.getSongList().isEmpty()){
                mHasMusic = true;
            }
        }

        //Launch ViewModel if savedInstanceState is null
        if(savedInstanceState == null){
            setupTrainingViewModel();
        } else {
            //Set back state before saving
            mTrainingTime = savedInstanceState.getLong(KeyUtils.TRAINING_TIME);
            mTimeCounter = savedInstanceState.getInt(KeyUtils.EXERCISE_TIME);
            mExerciseList = ListConverter.stringToExerciseList(savedInstanceState.getString(KeyUtils.EXERCISES_ARRAY));
            mExercisePosition = savedInstanceState.getInt(KeyUtils.EXERCISE_POSITION);
            mTrainingIsPaused = savedInstanceState.getBoolean(KeyUtils.TRAINING_IS_PAUSED);
            setupTrainingViewModel();
        }
        return rootView;
    }

    private void getCountdown(final List<Exercise> exerciseList){

        mCountDownTimer = new CountDownTimer(mTrainingTime, 1000) {

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
    private void setupTrainingViewModel(){
        TrainingViewModel trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        trainingViewModel.getExercises(getActivity(), mIdList).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                //Initially populate ui
                populateUi(mExercisePosition, exercises);
                //Launch Countdown
                mExerciseList = exercises;
                //If training is not paused, launch the countdown
                if(!mTrainingIsPaused){
                    getCountdown(mExerciseList);
                }
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
        //If Training is Paused set the Start/Pause Button to start
        if(mTrainingIsPaused){
            mStartPauseIV.setImageResource(R.drawable.start);
            //Set total time
            mCountdown.setText(""+String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes( mTrainingTime),
                    TimeUnit.MILLISECONDS.toSeconds(mTrainingTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mTrainingTime))));
        }

        //Set Button Actions
        setupPauseButton();
        setupVideoButton(currentExercise);
        setupMusicButton();

        //Start Music
        if(mHasMusic){
            //Initialize ExoPlayer
            mExoPlayer = ExoPlayerUtils.initializeExoPlayer(getActivity(), mExoPlayer, mPlaylist, this);
            //initializePlayer(ExoPlayerUtils.getMediaSourcePlaylist(getActivity(), mPlaylist.getSongList()));


            //Set player position if same playlist as last time
            if(mPlaylist.getPrimaryKey() == SharedPrefsUtils.getPlaylistId()){
                long exoPlayerPosition = SharedPrefsUtils.getExoPlayerPosition();
                if(exoPlayerPosition != 0 && exoPlayerPosition != 0){
                    mExoPlayer.seekTo(exoPlayerPosition);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
        if(!mTrainingIsPaused){
            stopCountdown();
        }

        if(mHasMusic){
            //Save Exoplayer position and Playlist Id to shared prefs
            SharedPrefsUtils.saveExoPlayerPosition(mExoPlayer.getCurrentPosition());
            SharedPrefsUtils.savePlaylistId(mPlaylist.getPrimaryKey());

            //Pause ExoPlayer
            mExoPlayer.setPlayWhenReady(false);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mHasMusic){
            //Release ExoPlayer
            mExoPlayer.release();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KeyUtils.EXERCISE_POSITION, mExercisePosition);
        outState.putInt(KeyUtils.EXERCISE_TIME, mTimeCounter);
        outState.putString(KeyUtils.EXERCISES_ARRAY, ListConverter.exerciseListToString(mExerciseList));
        outState.putLong(KeyUtils.TRAINING_TIME, mTrainingTime);
        outState.putBoolean(KeyUtils.TRAINING_IS_PAUSED, mTrainingIsPaused);
        super.onSaveInstanceState(outState);

    }

    private void playVoiceCommand(int audioId){
        //If there is no command audioId is 0, so don't launch MediaPlayer then
        if(audioId != 0){
            releaseMediaPlayer();
            mCommandMediaPlayer = MediaPlayer.create(getActivity(), audioId);
            mCommandMediaPlayer.start();
            //Duck ExoPlayer if User set in Settings
            if(SharedPrefsUtils.getDuckMusicBoolean()){
                ExoPlayerUtils.handleExoPlayerVolumeOnVoiceCommand(mExoPlayer, mCommandMediaPlayer);
            }
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

    private void setupPauseButton(){
        mStartPauseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTrainingIsPaused){
                    mStartPauseIV.setImageResource(R.drawable.pause);
                    mTrainingIsPaused = false;
                    getCountdown(mExerciseList);
                } else {
                    mStartPauseIV.setImageResource(R.drawable.start);
                    mTrainingIsPaused = true;
                    stopCountdown();
                }
            }
        });
    }

    private void setupVideoButton(final Exercise exercise){
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ckech for Internet Connection
                if(NetworkUtils.isConnectedToInternet()){
                    //Check for video key
                    if(exercise.getVideoUrl() != null){
                        String webUrl = getString(R.string.youtube_web_url )+ exercise.getVideoUrl();
                        String appUrl = getString(R.string.youtube_app_url )+ exercise.getVideoUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(webUrl));
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            startActivity(webIntent);
                        }
                    }else {
                        Toast.makeText(getActivity(), getString(R.string.no_exercise_video), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupMusicButton(){
        mMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseStartPlayer();
            }
        });

        mMusicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.cust_menu_title));

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.exoplayer_control_dialog, null);
                builder.setView(customLayout);

                // add a button
                builder.setPositiveButton(getString(R.string.positive_answer), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void pauseStartPlayer(){
        if(mExoPlayer != null){
            if(mExoPlayer.getPlayWhenReady()){
                mExoPlayer.setPlayWhenReady(false);
            } else {
                mExoPlayer.setPlayWhenReady(true);
            }
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}


