package com.example.android.vacuumfitness.utils;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.vacuumfitness.model.Training;

public class PreparationUtils {

    private static Resources res = Resources.getSystem();

    public static void setExerciseCount(final TextView exNumber, final ImageView increaseButton, final ImageView decreaseButton,
                                        final TextView timeTextView, final Spinner levelSpinner, final Training training){
        //Set max number for exerciseCount
        final int maxCount = getExerciseCountMaximum(training);

        //Set visibility to initialize
        if(training.getPrimaryKey() < 0){
            increaseButton.setVisibility(View.VISIBLE);
            decreaseButton.setVisibility(View.VISIBLE);
        } else if(maxCount < 2){
            increaseButton.setVisibility(View.INVISIBLE);
            decreaseButton.setVisibility(View.INVISIBLE);
        } else {
            increaseButton.setVisibility(View.INVISIBLE);
            decreaseButton.setVisibility(View.VISIBLE);
        }

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(exNumber.getText().toString());
                count++;
                exNumber.setText(String.valueOf(count));
                calculateTime(exNumber, timeTextView, levelSpinner);

                //Hide increase button if count bigger as 9
                if (count > maxCount - 1){
                    increaseButton.setVisibility(View.INVISIBLE);
                } else if(count > 1) {
                    decreaseButton.setVisibility(View.VISIBLE);
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(exNumber.getText().toString());
                count--;
                exNumber.setText(String.valueOf(count));
                calculateTime(exNumber, timeTextView,levelSpinner);

                //Hide increase button if count bigger as 9
                if (count < 2){
                    decreaseButton.setVisibility(View.INVISIBLE);
                } else if(count < maxCount){
                    increaseButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public  static void calculateTime(TextView exerciseCount, TextView timeCount, Spinner spinner){
        int timePerExercise = TrainingTimerUtils.exerciseTime(spinner.getSelectedItemPosition());

        int exercises = Integer.parseInt(exerciseCount.getText().toString());

        int totalTime = exercises * timePerExercise;

        String timeString = secondsToTimeString(totalTime);

        timeCount.setText(timeString);
    }

    public static String secondsToTimeString(int seconds){
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public static int getExerciseCount(Training training){
        int exerciseCount;

        if(training.getPrimaryKey() < 0){
            exerciseCount = 3;
        } else {
            exerciseCount = training.getExerciseList().size();
        }

        return exerciseCount;
    }

    private static int getExerciseCountMaximum(Training training){
        int exerciseCountMax;

        if(training.getPrimaryKey() < 0){
            exerciseCountMax = 10;
        } else {
            exerciseCountMax = training.getExerciseList().size();
        }

        return exerciseCountMax;
    }

}
