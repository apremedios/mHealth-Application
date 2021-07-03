package com.example.myproject.goalactivities;

public class Task {

    private long ID;
    private long goal_id;
    private String title;
    private String notes;
    long complete = 0;    //0 is false by default

    public Task(){};

    //for creating tasks
    public Task(long goal_id, String title, String notes){
        this.goal_id = goal_id;
        this.title = title;
        this.notes = notes;
    };

    public Task(long ID, long goal_id, String title, String notes, long complete){
        this.ID = ID;
        this.goal_id = goal_id;
        this.title = title;
        this.notes = notes;
        this.complete = complete;
    };

    //for database retrieval

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(long goal_id) {
        this.goal_id = goal_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getComplete() {
        return complete;
    }

    public void setComplete(long complete) {
        this.complete = complete;
    }
}
