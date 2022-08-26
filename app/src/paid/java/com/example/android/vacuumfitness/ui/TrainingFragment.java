package com.example.android.vacuumfitness.ui;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.example.android.vacuumfitness.utils.ExoPlayerUtils;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.ListConverter;
import com.example.android.vacuumfitness.utils.NetworkUtils;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;
import com.example.android.vacuumfitness.utils.TrainingTimerUtils;
import com.example.android.vacuumfitness.viewmodel.TrainingViewModel;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
    @BindView(R.id.tv_exercise_count_of) TextView mExerciseCountTextView;
    @BindView(R.id.tv_exercise_list_name) TextView mExerciseName;
    @BindView(R.id.iv_exercise_image) ImageView mExerciseImage;
    @BindView(R.id.iv_start_pause) ImageView mStartPauseIV;
    @BindView(R.id.iv_video_button) ImageView mVideoButton;
    @BindView(R.id.iv_music_button) ImageView mMusicButton;
    @BindView(R.id.back_button) ImageView mBackButton;

    private int mExerciseCount;
    private int level;

    private int mTimeCounter = 0;
    private int mExercisePosition = 1;
    private List<Exercise> mExerciseList;
    private long mTrainingTime;
    private CountDownTimer mCountDownTimer;
    private boolean mTrainingIsPaused;
    private List<Integer> mIdList;

    private MediaPlayer mCommandMediaPlayer;

    private Playlist mPlaylist;
    private SimpleExoPlayer mExoPlayer;
    private boolean mHasMusic = false;
    private boolean mHasVoiceCommands;
    private boolean mHasVisualCommands;
    private boolean mExoPlayerIsPaused = false;

    private AppDatabase mDb;

    private View mToastLayout;

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

        //Setup Back Button
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //Get booleans for voice and visual commands from settings
        mHasVoiceCommands = SharedPrefsUtils.getVoiceToggleBoolean(getActivity());
        mHasVisualCommands = SharedPrefsUtils.getVisualToggleBoolean(getActivity());

        //Prepare toast layout
        LayoutInflater toastInflater = getLayoutInflater();
        mToastLayout = inflater.inflate(R.layout.command_toast, (ViewGroup)rootView.findViewById(R.id.layout_command_toast));

        //Get Data from Bundle
        Bundle data = getArguments();
        if(data != null && data.containsKey(KeyUtils.EXERCISE_COUNT_KEY) && data.containsKey(KeyUtils.LEVEL_KEY)){
            mExerciseCount = data.getInt(KeyUtils.EXERCISE_COUNT_KEY);
            level = data.getInt(KeyUtils.LEVEL_KEY);
            mIdList = ListConverter.fromString(data.getString(KeyUtils.ID_LIST_KEY));
            mPlaylist = data.getParcelable(KeyUtils.PLAYLIST_KEY);
        }

        //Make the Training time
        mTrainingTime = TrainingTimerUtils.getTrainingTimeMilliseconds(level, mExerciseCount);

        //Prepare the Media Source for Exoplayer
        if(mPlaylist != null){
            //Playlist is no null, now we check if these is a Songlist with songs inside
            if(mPlaylist.getSongList() != null && !mPlaylist.getSongList().isEmpty()){
                mHasMusic = true;
            } else {
                //Set nomusic picture if there is no music
                mMusicButton.setImageResource(R.drawable.nomusic);
            }
        } else {
            //Set nomusic picture if there is no music
            mMusicButton.setImageResource(R.drawable.nomusic);
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
            mExoPlayerIsPaused = savedInstanceState.getBoolean(KeyUtils.EXOPLAYER_IS_PAUSED);
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

                //Give Voice Commands if user wants
                if(mHasVoiceCommands){
                    int voiceCommand = TrainingTimerUtils.getVoiceCommandInt(TrainingTimerUtils.getCommandCorners(level), mTimeCounter, mTrainingTime, getActivity());
                    playVoiceCommand(voiceCommand);
                }

                //Give visual commands if user wants
                if(mHasVisualCommands){
                    String visualCommand = TrainingTimerUtils.getVisualCommandString(TrainingTimerUtils.getCommandCorners(level), mTimeCounter, mTrainingTime, getActivity());
                    makeCommandToast(visualCommand);
                }


                if (mTimeCounter < TrainingTimerUtils.exerciseTime(level)){
                    mTimeCounter++;
                } else {
                    mTimeCounter = 0;
                    mExercisePosition++;
                    populateUi(mExercisePosition, exerciseList);
                }
            }

            public void onFinish() {
                //If training is finished we can finish the training activity
                getActivity().finish();
            }
        }.start();
    }

    private void stopCountdown(){
        mCountDownTimer.cancel();
    }

    //Setup ViewModel
    private void setupTrainingViewModel(){
        TrainingViewModel trainingViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);
        trainingViewModel.getExercises(getActivity(), mIdList).observe(getViewLifecycleOwner(), new Observer<List<Exercise>>() {
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
        mExerciseCountTextView.setText(TrainingTimerUtils.makeExerciseCountString(counter, mExerciseCount));
        //Set exercise name
        mExerciseName.setText(currentExercise.getExerciseName());
        //Set exercise image
        String imagePath = currentExercise.getImage();
        int resId = TrainingTimerUtils.getResId(imagePath, R.drawable.class);
        if (resId != -1) {
            Picasso.get().load(resId).into(mExerciseImage);
        } else {
            //If path not found load a dummy picture
            Picasso.get().load(R.drawable.eagle).into(mExerciseImage);
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
    }

    //Check if hast music and start it
    private void startMusic() {
        //Start Music
        if(mHasMusic){
            //Initialize ExoPlayer
            mExoPlayer = ExoPlayerUtils.initializeExoPlayer(getActivity(), mExoPlayer, mPlaylist, this);
            //initializePlayer(ExoPlayerUtils.getMediaSourcePlaylist(getActivity(), mPlaylist.getSongList()));

            int playlistSize = mPlaylist.getSongList().size();

            //Set player position if same playlist as last time
            if(mPlaylist.getPrimaryKey() == SharedPrefsUtils.getPlaylistId(getActivity())){
                long exoPlayerPosition = SharedPrefsUtils.getExoPlayerPosition(getActivity());
                int exoPlayerIndex =  SharedPrefsUtils.getExoPlayerIndex(getActivity());
                if(exoPlayerPosition != 0 && exoPlayerIndex < playlistSize){
                    mExoPlayer.seekTo(exoPlayerIndex, exoPlayerPosition);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //If hasMusic kick off the music
        startMusic();

        //If Training is paused pause the music
        if(mHasMusic && mTrainingIsPaused){
            pauseStartPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //Pause the Training
        if(!mTrainingIsPaused){
            pauseStartTraining();
        }

        if(mHasMusic){
            //Save Exoplayer position, index and Playlist Id to shared prefs
            SharedPrefsUtils.saveExoPlayerPosition(getActivity(), mExoPlayer.getCurrentPosition());
            SharedPrefsUtils.saveExoPlayerIndex(getActivity(), mExoPlayer.getCurrentWindowIndex());
            SharedPrefsUtils.savePlaylistId(getActivity(), mPlaylist.getPrimaryKey());

            //Pause ExoPlayer
            mExoPlayer.setPlayWhenReady(false);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseMediaPlayer();
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
        outState.putBoolean(KeyUtils.EXOPLAYER_IS_PAUSED, mExoPlayerIsPaused);
        super.onSaveInstanceState(outState);
    }

    private void playVoiceCommand(int audioId){
        //If there is no command audioId is 0, so don't launch MediaPlayer then
        if(audioId != 0){
            releaseMediaPlayer();
            mCommandMediaPlayer = MediaPlayer.create(getActivity(), audioId);
            mCommandMediaPlayer.start();
            //Duck ExoPlayer if User set in Settings
            if(SharedPrefsUtils.getDuckMusicBoolean(getActivity())){
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
                pauseStartTraining();
            }
        });
    }

    private void pauseStartTraining(){

        if(mTrainingIsPaused){
            mStartPauseIV.setImageResource(R.drawable.pause);
            mTrainingIsPaused = false;
            getCountdown(mExerciseList);

            //If ExoPlayer is not null and was not paused before pause training, start the music also
            if(mExoPlayer !=null && !mExoPlayerIsPaused){
                pauseStartPlayer();
            }

        } else {
            mStartPauseIV.setImageResource(R.drawable.start);
            mTrainingIsPaused = true;
            stopCountdown();

            //If ExoPlayer is not null ans playing also pause him
            if(mExoPlayer != null && mExoPlayer.getPlayWhenReady()){
                pauseStartPlayer();
                mExoPlayerIsPaused = false;
            } else {
                mExoPlayerIsPaused = true;
            }
        }

    }

    private void setupVideoButton(final Exercise exercise){
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for Internet Connection
                if(NetworkUtils.isConnectedToInternet()){
                    //Check for video key
                    if(exercise.getVideoUrl() != null && !TextUtils.isEmpty(exercise.getVideoUrl())){
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
    }

    private void pauseStartPlayer(){
        if(mExoPlayer != null){
            if(mExoPlayer.getPlayWhenReady()){
                mExoPlayer.setPlayWhenReady(false);
                //Set music picture
                mMusicButton.setImageResource(R.drawable.nomusic);
            } else {
                mExoPlayer.setPlayWhenReady(true);
                //Set music picture
                mMusicButton.setImageResource(R.drawable.music);
            }
        }
    }

    private void makeCommandToast(String toastText){
        if (!TextUtils.isEmpty(toastText)){
            TextView text = (TextView) mToastLayout.findViewById(R.id.tv_toast_text);
            text.setText(toastText);

            Toast toast = new Toast(getActivity().getApplicationContext());
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(mToastLayout);
            toast.show();
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
        Log.d(LOG_TAG, error.toString());
        error.printStackTrace();
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


