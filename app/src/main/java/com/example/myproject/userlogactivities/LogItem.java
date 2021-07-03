package com.example.myproject.userlogactivities;

public class LogItem {

    private long ID;
    private long thing_id;
    private String thing_type;
    private String title;
    private String date;
    private String time;

    public LogItem(){}

    public LogItem(long id, long thing_id, String thing_type,String title, String date, String time){
        this.ID = id;
        this.thing_id = thing_id;
        this.thing_type = thing_type;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getThing_id() {
        return thing_id;
    }

    public void setThing_id(long thing_id) {
        this.thing_id = thing_id;
    }

    public String getThing_type() {
        return thing_type;
    }

    public void setThing_type(String thing_type) {
        this.thing_type = thing_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
