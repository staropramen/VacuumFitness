package com.example.android.vacuumfitness.utils;

import android.content.Context;
import android.content.res.Resources;
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

    private static int prepare = 8;
    private static int inhale = 2;
    private static int exhale = 4;
    private static int rest = 6;
    private static int relax = 8;
    private static int vacuumBeginner = 15;
    private static int vacuumIntermediate = 20;
    private static int vacuumAdvanced = 25;

    //Return time per exercise based on level
    public static int exerciseTime(int level){
        int vacuum;
        if(level == 0){
            vacuum = vacuumBeginner;
        }else if (level == 1){
            vacuum = vacuumIntermediate;
        }else {
            vacuum = vacuumAdvanced;
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
        int vacuum;
        if(level == 1){
            vacuum = vacuumBeginner;
        }else if (level == 2){
            vacuum = vacuumIntermediate;
        }else {
            vacuum = vacuumAdvanced;
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

        int vacuum;
        if(level == 0){
            vacuum = vacuumBeginner;
        }else if (level == 1){
            vacuum = vacuumIntermediate;
        }else {
            vacuum = vacuumAdvanced;
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


    public static int getVoiceCommandInt(int[] cornerStones, int counter, long trainingTime, Context context){

        if(counter == cornerStones[0]){
            return R.raw.take_position;
        } else if(counter == cornerStones[1]){
            return R.raw.inhale;
        } else if(counter == cornerStones[2]){
            return R.raw.exhale;
        } else if(counter == cornerStones[3]){
            return R.raw.inhale;
        } else if(counter == cornerStones[4]){
            return R.raw.exhale;
        } else if(counter == cornerStones[5]){
            return R.raw.vacuum;
        } else if(counter == cornerStones[6]){
            return R.raw.relax;
        } else if(counter == cornerStones[7]){
            return R.raw.inhale;
        } else if(counter == cornerStones[8]){
            return R.raw.exhale;
        } else if(counter == cornerStones[9]){
            return R.raw.inhale;
        } else if(counter == cornerStones[10]){
            return R.raw.exhale;
        } else if(counter == cornerStones[11]){
            return R.raw.vacuum;
        } else if(counter == cornerStones[12]){
            //If training is up to finish here we return a finish raw file
            if(trainingTime < 20000){
                return R.raw.finish;
            } else {
                return R.raw.change;
            }
        } else {
            //If counter is not at cornerstone position return 0
            return 0;
        }

    }

    public static String getVisualCommandString(int[] cornerStones, int counter, long trainingTime, Context context){
        String visualCommand = "";
        Resources res = context.getResources();

        if(counter == cornerStones[0]){
            return res.getString(R.string.prepare);
        } else if(counter == cornerStones[1]){
            return res.getString(R.string.inhale);
        } else if(counter == cornerStones[2]){
            return res.getString(R.string.exhale);
        } else if(counter == cornerStones[3]){
            return res.getString(R.string.inhale);
        } else if(counter == cornerStones[4]){
            return res.getString(R.string.exhale);
        } else if(counter == cornerStones[5]){
            return res.getString(R.string.vacuum);
        } else if(counter == cornerStones[6]){
            return res.getString(R.string.relax);
        } else if(counter == cornerStones[7]){
            return res.getString(R.string.inhale);
        } else if(counter == cornerStones[8]){
            return res.getString(R.string.exhale);
        } else if(counter == cornerStones[9]){
            return res.getString(R.string.inhale);
        } else if(counter == cornerStones[10]){
            return res.getString(R.string.exhale);
        } else if(counter == cornerStones[11]){
            return res.getString(R.string.vacuum);
        } else if(counter == cornerStones[12]){
            //If training is up to finish here we return a finish raw file
            if(trainingTime < 20000){
                return res.getString(R.string.finish);
            } else {
                return res.getString(R.string.change);
            }

        } else {
            //If counter is not at cornerstone position return empty string
            return visualCommand;
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
