package com.example.android.vacuumfitness.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "motivators")
public class Motivator {

    @PrimaryKey(autoGenerate = false)
    private int primaryKey;
    @ColumnInfo(name = "motivation_text")
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
