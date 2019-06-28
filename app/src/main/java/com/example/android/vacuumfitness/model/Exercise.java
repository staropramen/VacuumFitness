package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "exercises")
public class Exercise implements Parcelable {

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

    //Constructor for Parcel
    @Ignore
    public Exercise(Parcel in) {
        primaryKey = in.readInt();
        exerciseName = in.readString();
        level = in.readInt();
        image = in.readString();
        videoUrl = in.readString();
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

    @Override
    public boolean equals(Object object) {
        boolean isEqual= false;

        if (object != null && object instanceof Exercise)
        {
            isEqual = (this.primaryKey == ((Exercise) object).primaryKey);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.primaryKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryKey);
        dest.writeString(exerciseName);
        dest.writeInt(level);
        dest.writeString(image);
        dest.writeString(videoUrl);
    }

    @Ignore
    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
