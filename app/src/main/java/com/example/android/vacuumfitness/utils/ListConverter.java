package com.example.android.vacuumfitness.utils;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.example.android.vacuumfitness.model.Exercise;
import com.example.android.vacuumfitness.model.Training;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public static String fromList(List<Integer> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Integer> fromString(String string){
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public static String exerciseListToString(List<Exercise> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Exercise> stringToExerciseList(String string){
        Type listType = new TypeToken<List<Exercise>>() {}.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public static String mediaSourceToString(ConcatenatingMediaSource mediaSource){
        Gson gson = new Gson();
        String json = gson.toJson(mediaSource);
        return json;
    }

    @TypeConverter
    public static ConcatenatingMediaSource stringToMediaSource(String string){
        Type type = new TypeToken<ConcatenatingMediaSource>() {}.getType();
        return new Gson().fromJson(string, type);
    }

}