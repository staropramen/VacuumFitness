package com.example.android.vacuumfitness.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PreparationUtils {

    public static void setExerciseCount(final TextView exNumber, final ImageView increaseButton, final ImageView decreaseButton){
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(exNumber.getText().toString());
                count++;
                exNumber.setText(String.valueOf(count));

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

                //Hide increase button if count bigger as 9
                if (count < 2){
                    decreaseButton.setVisibility(View.INVISIBLE);
                } else if(count < 10){
                    increaseButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public  static void calculateTime(TextView exerciseCount, TextView timeCount){

    }

}
