package com.example.myproject.goalactivities;

public class Check {

    private long ID;
    private long task_id;
    private String title;
    long complete = 0;    //0 is false by default

    public Check(){};

    //for creating tasks
    public Check(long task_id, String title){
        this.task_id = task_id;
        this.title = title;
    };

    public Check(long ID, long task_id, String title, long complete){
        this.ID = ID;
        this.task_id = task_id;
        this.title = title;
        this.complete = complete;
    };

    //for database retrieval

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getComplete() {
        return complete;
    }

    public void setComplete(long complete) {
        this.complete = complete;
    }
}