package com.example.myproject.goalactivities;

import com.example.myproject.DBManager;

public class Goal {
    private long ID;
    private String title;
    private String motivation;
    private String date;
    private String time;

    public Goal(){ }
    public Goal(String title, String motivation, String date, String time){
        this.title = title;
        this.motivation = motivation;
        this.date = date;
        this.time = time;
    }

    public Goal(long id, String title, String motivation, String date, String time){
        this.ID = id;
        this.title = title;
        this.motivation = motivation;
        this.date = date;
        this.time = time;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
