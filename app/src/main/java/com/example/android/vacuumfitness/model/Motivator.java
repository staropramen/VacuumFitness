package com.example.android.vacuumfitness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "motivators")
public class Motivator {

    @PrimaryKey(autoGenerate = false)
    private int primaryKey;
    @ColumnInfo(name = "motivation-text")
    private String motivationText;

    public Motivator(int primaryKey, String motivationText) {
        this.primaryKey = primaryKey;
        this.motivationText = motivationText;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getMotivationText() {
        return motivationText;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setMotivationText(String motivationText) {
        this.motivationText = motivationText;
    }
}
