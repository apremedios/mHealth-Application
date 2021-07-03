package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myproject.goalactivities.Check;
import com.example.myproject.goalactivities.Goal;
import com.example.myproject.goalactivities.Task;
import com.example.myproject.noteactivites.Note;
import com.example.myproject.scheduleactivities.ScheduleItem;
import com.example.myproject.userlogactivities.LogItem;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    //UserNotes table variables

    public DBManager(@Nullable Context context) {
        super(context, "Userdata.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //UserNotesTable
        db.execSQL("create Table UserNotes(" +
                "id INTEGER primary key AUTOINCREMENT NOT NULL, " +
                "title TEXT, " +
                "text TEXT, " +
                "date TEXT, " +
                "time TEXT)"
        );
        //User Goals Table and associated 'Tasks' and 'Task Check list Items' tables
        db.execSQL("create Table UserGoals(id INTEGER primary key AUTOINCREMENT NOT NULL, title TEXT, motivation TEXT, date TEXT, time TEXT, complete INTEGER NOT NULL DEFAULT 0)");
        //tasks
        db.execSQL("create Table UserTasks(id INTEGER primary key AUTOINCREMENT NOT NULL, goal_id INTEGER NOT NULL, title TEXT, notes TEXT, complete INTEGER NOT NULL DEFAULT 0)");
        //checklist
        db.execSQL("create Table UserCheckList(id INTEGER primary key AUTOINCREMENT NOT NULL, task_id INTEGER NOT NULL, title TEXT, complete INTEGER NOT NULL DEFAULT 0)");
        //log
        db.execSQL("create Table UserLog(id INTEGER primary key AUTOINCREMENT NOT NULL, thing_id INTEGER NOT NULL, thing_type TEXT NOT NULL, title TEXT, date TEXT, time TEXT)");
        //schedule --- long id, String title, String note, String timeStart, String timeEnd, long type
        db.execSQL("create Table UserSchedule(id INTEGER primary key AUTOINCREMENT NOT NULL, " +
                "timestamp INTEGER NOT NULL, " +
                "title TEXT, note TEXT, " +
                "date TEXT, " +
                "time_start INTEGER NOT NULL, " +
                "time_end INTEGER NOT NULL, " +
                "type INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //user notes
        db.execSQL("drop Table if exists UserNotes");
        //user goals, tasks, checklists
        db.execSQL("drop Table if exists UserGoals");
        db.execSQL("drop Table if exists UserTasks");
        db.execSQL("drop Table if exists UserCheckList");
        //user log
        db.execSQL("drop Table if exists UserLog");
        //user schedule
        db.execSQL("drop Table if exists UserSchedule");
    }

    //methods for ***GOALS***
    public long addGoal(Goal goal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("title", goal.getTitle());
        c.put("motivation", goal.getMotivation());
        c.put("date", goal.getDate());
        c.put("time", goal.getTime());

        long id = db.insert("UserGoals", null, c);
        return id;
    }

    public Goal getGoal(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserGoals where id = ?", new String[] {String.valueOf(id)});
        if(cursor != null){
            cursor.moveToFirst();
        }
        Goal goal = new Goal(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
        return goal;
    };

    public List<Goal> getGoals(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Goal> allGoals = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from UserGoals order by id desc", null);
        if (cursor.moveToFirst()){
            do{
                Goal goal = new Goal();
                goal.setID(cursor.getLong(0));
                goal.setTitle(cursor.getString(1));
                goal.setMotivation(cursor.getString(2));
                goal.setDate(cursor.getString(3));
                goal.setTime(cursor.getString(4));
                allGoals.add(goal);

            }while(cursor.moveToNext());

        }
        return allGoals;
    };

    public List<Goal> getCompleteGoals(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Goal> allGoals = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from UserGoals where complete = 1 and id != 1 order by id desc", null);
        if (cursor.moveToFirst()){
            do{
                Goal goal = new Goal();
                goal.setID(cursor.getLong(0));
                goal.setTitle(cursor.getString(1));
                goal.setMotivation(cursor.getString(2));
                goal.setDate(cursor.getString(3));
                goal.setTime(cursor.getString(4));
                allGoals.add(goal);

            }while(cursor.moveToNext());

        }
        return allGoals;
    };

    public List<Goal> getIncompleteGoals(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Goal> allGoals = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from UserGoals where complete = 0 and id != 1 order by id desc", null);
        if (cursor.moveToFirst()){
            do{
                Goal goal = new Goal();
                goal.setID(cursor.getLong(0));
                goal.setTitle(cursor.getString(1));
                goal.setMotivation(cursor.getString(2));
                goal.setDate(cursor.getString(3));
                goal.setTime(cursor.getString(4));
                allGoals.add(goal);

            }while(cursor.moveToNext());

        }
        return allGoals;
    };

    public int editGoal(Goal goal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("title", goal.getTitle());
        c.put("motivation", goal.getMotivation());
        c.put("date", goal.getDate());
        c.put("time", goal.getTime());
        return db.update("UserGoals", c, "id"+"=?", new String[]{String.valueOf(goal.getID())});
    }

    public int setGoalComplete(Goal goal, int complete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("complete", complete);

        if(complete == 1){
            addCompletedItem(goal.getID(), "goal", goal.getTitle());
        } else if (complete == 0){
            removeUncompletedItem(goal.getID(), "goal", goal.getTitle());
        }

        return db.update("UserGoals", c, "id" + "=?", new String[]{String.valueOf(goal.getID())});
    }

    public void deleteGoal(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserGoals", "id"+"=?", new String[]{String.valueOf(id)});
        db.delete("UserTasks", "goal_id"+"=?", new String[]{String.valueOf(id)});
        db.delete("UserCheckList", "task_id"+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //tasks --- id INTEGER primary key AUTOINCREMENT NOT NULL, goal_id INTEGER NOT NULL, title TEXT, notes TEXT, complete INTEGER NOT NULL DEFAULT 0
    public long addTask(Task task, long parent_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("goal_id", parent_id);
        c.put("title", task.getTitle());
        c.put("notes", task.getNotes());

        long id = db.insert("UserTasks", null, c);
        return id;
    }

    public Task getTask(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserTasks where id = ?", new String[] {String.valueOf(id)});

        if(cursor != null){
            cursor.moveToFirst();
        }
        Task task = new Task(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getLong(4));
        return task;
    }

    public List<Task> getTasks(long goal_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> allTasks = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * FROM (Select * from UserTasks where goal_id = ? order by id desc) order by complete asc", new String[] {String.valueOf(goal_id)});
        if (cursor.moveToFirst()){
            do{
                Task task = new Task();
                task.setID(cursor.getLong(0));
                task.setGoal_id(cursor.getLong(1));
                task.setTitle(cursor.getString(2));
                task.setNotes(cursor.getString(3));
                task.setComplete(cursor.getLong(4));
                allTasks.add(task);

            }while(cursor.moveToNext());

        }
        return allTasks;
    };

    public List<Task> getAllTasks(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> allTasks = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from UserTasks order by complete asc", null);
        if (cursor.moveToFirst()){
            do{
                Task task = new Task();
                task.setID(cursor.getLong(0));
                task.setGoal_id(cursor.getLong(1));
                task.setTitle(cursor.getString(2));
                task.setNotes(cursor.getString(3));
                task.setComplete(cursor.getLong(4));
                allTasks.add(task);

            }while(cursor.moveToNext());

        }
        return allTasks;
    }

    public int editTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("title", task.getTitle());
        c.put("notes", task.getNotes());
        c.put("complete", task.getComplete());
        return db.update("UserTasks", c, "id"+"=?", new String[]{String.valueOf(task.getID())});
    }

    public int setTaskComplete(Task task, int complete){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("complete", complete);

        if(complete == 1){
            addCompletedItem(task.getID(), "task", task.getTitle());
        } else if (complete == 0){
            removeUncompletedItem(task.getID(), "task", task.getTitle());
        }

        return db.update("UserTasks", c, "id"+"=?", new String[]{String.valueOf(task.getID())});
    }

    public void deleteTask(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserTasks", "id"+"=?", new String[]{String.valueOf(id)});
        db.delete("UserCheckList", "task_id"+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //checklist --- id INTEGER primary key AUTOINCREMENT NOT NULL, task_id INTEGER NOT NULL, title TEXT, complete INTEGER NOT NULL DEFAULT 0
    public long addCheck(Check check, long parent_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("task_id", parent_id);
        c.put("title", check.getTitle());

        long id = db.insert("UserCheckList", null, c);
        return id;
    }

    public Check getCheck(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserCheckList where id = ?", new String[] {String.valueOf(id)});

        if(cursor != null){
            cursor.moveToFirst();
        }
        Check check = new Check(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                cursor.getLong(3));
        return check;
    };

    public List<Check> getCheckList(long task_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Check> allChecks = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * FROM (Select * from UserCheckList where task_id = ? order by id desc) order by complete asc", new String[] {String.valueOf(task_id)});
        if (cursor.moveToFirst()){
            do{
                Check check = new Check();
                check.setID(cursor.getLong(0));
                check.setTask_id(cursor.getLong(1));
                check.setTitle(cursor.getString(2));
                check.setComplete(cursor.getLong(3));
                allChecks.add(check);

            }while(cursor.moveToNext());

        }
        return allChecks;
    };

    public int editCheck(Check check){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("title", check.getTitle());
        c.put("complete", check.getComplete());
        return db.update("UserCheckList", c, "id"+"=?", new String[]{String.valueOf(check.getID())});
    }

    public int setCheckComplete(Check check, int complete){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("complete", complete);
        return db.update("UserCheckList", c, "id"+"=?", new String[]{String.valueOf(check.getID())});
    }

    public void deleteCheck(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserCheckList", "id"+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //methods for ***NOTES***
    public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("title", note.getTitle());
        c.put("text", note.getText());
        c.put("date", note.getDate());
        c.put("time", note.getTime());

        long id = db.insert("UserNotes", null, c);
        return id;
    }

    public Note getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserNotes where id = ?", new String[] {String.valueOf(id)});

        if(cursor != null){
            cursor.moveToFirst();
        }
         Note note = new Note(
                 cursor.getLong(0),
                 cursor.getString(1),
                 cursor.getString(2),
                 cursor.getString(3),
                 cursor.getString(4));
        return note;
    };

    public List<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from UserNotes order by id desc", null);
        if (cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setText(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                allNotes.add(note);

            }while(cursor.moveToNext());

        }
        return allNotes;
    };

    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("title", note.getTitle());
        c.put("text", note.getText());
        c.put("date", note.getDate());
        c.put("time", note.getTime());
        return db.update("UserNotes", c, "id"+"=?", new String[]{String.valueOf(note.getID())});
    }

    public void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserNotes", "id"+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //UserLog --- id INTEGER primary key AUTOINCREMENT NOT NULL, millis INTEGER NOT NULL, title TEXT, note TEXT, date TEXT, time_start TEXT, time_end TEXT, type INTEGER NOT NULL DEFAULT 0
    public long addCompletedItem(long thing_id, String thing_type, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("thing_id", thing_id);
        c.put("thing_type", thing_type);
        c.put("title", "I completed my" + " " + thing_type + " : " + title);
        c.put("date", UsefulFunctions.getCurrentDate());
        c.put("time", UsefulFunctions.getCurrentTime());

        long id = db.insert("UserLog", null, c);
        return id;
    }

    public void removeUncompletedItem(long thing_id, String thing_type, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserLog", "thing_id = ? and thing_type = ?", new String[]{String.valueOf(thing_id), thing_type});
    }

    public List<LogItem> getLogItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<LogItem> logItems = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from UserLog order by id desc", null);
        if (cursor.moveToFirst()){
            do{
                LogItem logitem = new LogItem();
                logitem.setID(cursor.getLong(0));
                logitem.setThing_id(cursor.getLong(1));
                logitem.setThing_type(cursor.getString(2));
                logitem.setTitle(cursor.getString(3));
                logitem.setDate(cursor.getString(4));
                logitem.setTime(cursor.getString(5));
                logItems.add(logitem);

            }while(cursor.moveToNext());

        }
        return logItems;
    };

    //userSchedule --- id INTEGER primary key AUTOINCREMENT NOT NULL, timestamp INTEGER NOT NULL, title TEXT, note TEXT, date TEXT, time_start INTEGER, time_end INTEGER, type INTEGER NOT NULL DEFAULT 0
    public long addScheduleItem(ScheduleItem scheduleItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("timestamp", scheduleItem.getTimestamp());
        c.put("title", scheduleItem.getTitle());
        c.put("note", scheduleItem.getNote());
        c.put("date", scheduleItem.getDate());
        c.put("time_start", scheduleItem.getTimeStart());
        c.put("time_end", scheduleItem.getTimeEnd());
        c.put("type", scheduleItem.getType());

        long id = db.insert("UserSchedule", null, c);
        return id;
    }

    //0 = a non-repeating item
    //1 = repeating every day item
    //2 = repeating every week item
    //3 = repeating every month item
    public ScheduleItem getScheduleItem(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserSchedule where id = ?", new String[] {String.valueOf(id)});

        if(cursor != null){
            cursor.moveToFirst();
        }
        ScheduleItem scheduleItem = new ScheduleItem(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getLong(5),
                cursor.getLong(6),
                cursor.getLong(7));
        return scheduleItem;
    };

    public List<ScheduleItem> getScheduleItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from UserSchedule order by time_start asc", null); //always get items the correspond to the current date and repeat every day
        if (cursor.moveToFirst()){
            do{
                ScheduleItem scheduleItem = new ScheduleItem();
                scheduleItem.setID(cursor.getLong(0));
                scheduleItem.setTimestamp(cursor.getLong(1));
                scheduleItem.setTitle(cursor.getString(2));
                scheduleItem.setNote(cursor.getString(3));
                scheduleItem.setDate(cursor.getString(4));
                scheduleItem.setTimeStart(cursor.getLong(5));
                scheduleItem.setTimeEnd(cursor.getLong(6));
                scheduleItem.setType(cursor.getLong(7));
                scheduleItems.add(scheduleItem);

            }while(cursor.moveToNext());

        }
        return scheduleItems;
    }

    public void deleteScheduleItem(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserSchedule", "id"+"=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public int editScheduleItem(ScheduleItem scheduleItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("title", scheduleItem.getTitle());
        c.put("note", scheduleItem.getNote());
        c.put("time_start", scheduleItem.getTimeStart());
        c.put("time_end", scheduleItem.getTimeEnd());
        c.put("type", scheduleItem.getType());
        return db.update("UserSchedule", c, "id"+"=?", new String[]{String.valueOf(scheduleItem.getID())});
    }

    //db common
    public boolean holderGoalExists(){
        SQLiteDatabase db = this.getReadableDatabase();
        String search = "SELECT * FROM UserGoals WHERE id = 1";
        Cursor cursor;
        cursor = db.rawQuery(search, null);
        if (cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    public Goal getHolderGoal(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserGoals where id = 1", null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Goal goal = new Goal(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
        return goal;
    }
}
