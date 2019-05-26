package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int primaryKey;
    @ColumnInfo(name = "exercise_name")
    private String exerciseName;
    @ColumnInfo(name = "level")
    private int level;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "video_url")
    private String videoUrl;

    public Exercise(int primaryKey, String exerciseName, int level, String image, String videoUrl) {
        this.primaryKey = primaryKey;
        this.exerciseName = exerciseName;
        this.level = level;
        this.image = image;
        this.videoUrl = videoUrl;
    }

    @Ignore
    public Exercise(String exerciseName, int level, String image, String videoUrl) {
        this.exerciseName = exerciseName;
        this.level = level;
        this.image = image;
        this.videoUrl = videoUrl;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getLevel() {
        return level;
    }

    public String getImage() {
        return image;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
