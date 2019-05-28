package com.example.android.vacuumfitness.utils;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.vacuumfitness.R;

public class PreparationUtils {

    private static Resources res = Resources.getSystem();

    public static void setExerciseCount(final TextView exNumber, final ImageView increaseButton, final ImageView decreaseButton,
                                        final TextView timeTextView, final Spinner levelSpinner){
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(exNumber.getText().toString());
                count++;
                exNumber.setText(String.valueOf(count));
                calculateTime(exNumber, timeTextView, levelSpinner);

                //Hide increase button if count bigger as 9
                if (count > 9){
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
                } else if(count < 10){
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

    private static String secondsToTimeString(int seconds){
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }



}
