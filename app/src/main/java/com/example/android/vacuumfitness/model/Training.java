package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "trainings")
public class Training {

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
}
