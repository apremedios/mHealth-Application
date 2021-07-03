package com.example.myproject.scheduleactivities;

public class ScheduleItem {

    private long ID;
    private long timestamp;
    private String title;
    private String note;
    private String date;
    private long timeStart;
    private long timeEnd;
    private long type; //0 is once, 1 is repeating, other types can be reintroduced by making this a long

    public ScheduleItem(){}

    public ScheduleItem(long timestamp, String title, String note, String date, long timeStart, long timeEnd, long type){
        this.timestamp = timestamp;
        this.title = title;
        this.note = note;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.type = type;
    }

    public ScheduleItem(long id, long timestamp,String title, String note, String date, long timeStart, long timeEnd, long type){
        this.ID = id;
        this.timestamp = timestamp;
        this.title = title;
        this.note = note;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.type = type;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
