package com.example.policia;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "incidents")
public class Incident implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String date;
    private String description;
    private String photoPath;
    private String audioPath;

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
