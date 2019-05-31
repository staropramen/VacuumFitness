package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vacuumfitness.R;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TrainingTimerUtils {

    //Return time per exercise based on level
    public static int exerciseTime(int level){
        int prepare = 10;
        int inhale = 2;
        int exhale = 4;
        int rest = 8;
        int relax = 13;
        int vacuum;
        if(level == 0){
            vacuum = 15;
        }else if (level == 1){
            vacuum = 22;
        }else {
            vacuum = 30;
        }

        int time = prepare + 4 * inhale + 4 * exhale + 2 * vacuum + rest + relax;

        return time;
    }

    public static long getTrainingTimeMilliseconds(int level, int exercises){
        int timePerExercise = exerciseTime(level);
        int totalTime = exercises * timePerExercise;

        return totalTime * 1000;
    }


    //Functions that returns the actual state of the exercise as Integer
    private static int exerciseState(int counter, int level, int state){
        int prepare = 10;
        int inhale = 2;
        int exhale = 4;
        int rest = 8;
        int relax = 13;
        int vacuum;
        if(level == 1){
            vacuum = 15;
        }else if (level == 2){
            vacuum = 22;
        }else {
            vacuum = 30;
        }

        if(counter == 0){
            return prepare;
        } else if(counter == prepare){
            return inhale;
        } else if(counter == state + inhale){
            return exhale;
        } else if(counter == state + exhale){
            return inhale;
        } else if(counter == state + inhale){
            return exhale;
        }

        return inhale;
    }

    //Function returns array of integers tha are the corners for commands
    public static int[] getCommandCorners(int level){
        int prepare = 10;
        int inhale = 2;
        int exhale = 4;
        int rest = 8;
        int relax = 13;
        int vacuum;
        if(level == 0){
            vacuum = 15;
        }else if (level == 1){
            vacuum = 22;
        }else {
            vacuum = 30;
        }

        int stoneOne = prepare + inhale;
        int stoneTwo = stoneOne + exhale;
        int stoneThree = stoneTwo + inhale;
        int stoneFour = stoneThree + exhale;
        int stoneFive = stoneFour + vacuum;
        int stoneSix = stoneFive + rest;
        int stoneSeven = stoneSix + inhale;
        int stoneEight = stoneSeven + exhale;
        int stoneNine = stoneEight + inhale;
        int stoneTen = stoneNine + exhale;
        int stoneEleven = stoneTen + vacuum;

        int[] cornerStones = new int[] {0, prepare, stoneOne, stoneTwo, stoneThree, stoneFour,
        stoneFive, stoneSix, stoneSeven, stoneEight, stoneNine, stoneTen, stoneEleven};
        return cornerStones;
    }


    public static int getVoiceCommandInt(int[] cornerStones, int counter, Context context){

        if(counter == cornerStones[0]){
            //Toast.makeText(context, "Prepare", Toast.LENGTH_SHORT).show();
            return R.raw.take_position;
        } else if(counter == cornerStones[1]){
            //Toast.makeText(context, "Inhale", Toast.LENGTH_SHORT).show();
            return R.raw.inhale;
        } else if(counter == cornerStones[2]){
            //Toast.makeText(context, "Exhale", Toast.LENGTH_SHORT).show();
            return R.raw.exhale;
        } else if(counter == cornerStones[3]){
            //Toast.makeText(context, "Inhale", Toast.LENGTH_SHORT).show();
            return R.raw.inhale;
        } else if(counter == cornerStones[4]){
            //Toast.makeText(context, "Exhale", Toast.LENGTH_SHORT).show();
            return R.raw.exhale;
        } else if(counter == cornerStones[5]){
            //Toast.makeText(context, "Vacuum", Toast.LENGTH_SHORT).show();
            return R.raw.vacuum;
        } else if(counter == cornerStones[6]){
            //Toast.makeText(context, "Rest", Toast.LENGTH_SHORT).show();
            return R.raw.relax;
        } else if(counter == cornerStones[7]){
            //Toast.makeText(context, "Inhale", Toast.LENGTH_SHORT).show();
            return R.raw.inhale;
        } else if(counter == cornerStones[8]){
            //Toast.makeText(context, "Exhale", Toast.LENGTH_SHORT).show();
            return R.raw.exhale;
        } else if(counter == cornerStones[9]){
            //Toast.makeText(context, "Inhale", Toast.LENGTH_SHORT).show();
            return R.raw.inhale;
        } else if(counter == cornerStones[10]){
            //Toast.makeText(context, "Exhale", Toast.LENGTH_SHORT).show();
            return R.raw.exhale;
        } else if(counter == cornerStones[11]){
            //Toast.makeText(context, "Vacuum", Toast.LENGTH_SHORT).show();
            return R.raw.vacuum;
        } else if(counter == cornerStones[12]){
            //Toast.makeText(context, "Relax", Toast.LENGTH_SHORT).show();
            return R.raw.change;
        } else {
            //If counter is not at cornerstone position return 0
            return 0;
        }

    }

    public static String makeExerciseCountString(int current, int total){
        String exerciseCountString = String.valueOf(current) + "/" + String.valueOf(total);
        return exerciseCountString;
    }

    //Get ResId from String
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
