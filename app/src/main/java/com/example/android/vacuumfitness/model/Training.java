package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trainings")
public class Training implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int primaryKey;
    @ColumnInfo(name = "training_name")
    private String trainingName;
    @ColumnInfo(name = "label")
    private String label;
    @ColumnInfo(name = "exercise_list")
    private List<Exercise> exerciseList;

    public Training(int primaryKey, String trainingName, String label, List<Exercise> exerciseList) {
        this.primaryKey = primaryKey;
        this.trainingName = trainingName;
        this.label = label;
        this.exerciseList = exerciseList;
    }

    @Ignore
    public Training(String trainingName, String label, List<Exercise> exerciseList) {
        this.trainingName = trainingName;
        this.label = label;
        this.exerciseList = exerciseList;
    }

    @Ignore
    public Training(String trainingName, String label) {
        this.trainingName = trainingName;
        this.label = label;
    }

    //Empty Training for Error Catch
    @Ignore
    public Training() {
    }

    //Constructor for Parcel
    @Ignore
    public Training(Parcel in) {
        primaryKey = in.readInt();
        trainingName = in.readString();
        label = in.readString();
        exerciseList = new ArrayList<>();
        in.readList(exerciseList, Exercise.class.getClassLoader());
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public String getLabel() {
        return label;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryKey);
        dest.writeString(trainingName);
        dest.writeString(label);
        dest.writeList(exerciseList);
    }

    @Ignore
    public static final Parcelable.Creator<Training> CREATOR = new Parcelable.Creator<Training>() {
        @Override
        public Training createFromParcel(Parcel source) {
            return new Training(source);
        }

        @Override
        public Training[] newArray(int size) {
            return new Training[size];
        }
    };
}
