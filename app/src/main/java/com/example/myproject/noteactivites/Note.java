package com.example.myproject.noteactivites;

public class Note {
    private long ID;
    private String title;
    private String text;
    private String date;
    private String time;

    public Note(){ }
    public Note(String title, String text, String date, String time){
        this.title = title;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    public Note(long id, String title, String text, String date, String time){
        this.ID = id;
        this.title = title;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
