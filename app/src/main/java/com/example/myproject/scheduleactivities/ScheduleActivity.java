package com.example.myproject.scheduleactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myproject.AppListener;
import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.SecondActivity;
import com.example.myproject.TimerActivity;
import com.example.myproject.goalactivities.AdapterTasks;
import com.example.myproject.goalactivities.AddGoal;
import com.example.myproject.goalactivities.GoalsActivity;
import com.example.myproject.goalactivities.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AppListener {

    Button openCalendar, addCustom, setTimeStart, setTimeEnd;
    TextView tvDate;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    RecyclerView recyclerView;
    AdapterSchedule adapterSchedule;
    Toolbar toolbar;
    List<ScheduleItem> scheduleItems;
    long timeStamp;
    int hour, timeStartHour, timeStartMinute, timeEndHour, timeEndMinute;
    int minute;
    //if coming from a task to schedule
    Task task;
    //editing
    boolean editing;
    long scheduleItemId;
    ScheduleItem scheduleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //text views
        tvDate = findViewById(R.id.tvDate);
        //set date text view to match today
        Calendar c = Calendar.getInstance();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        tvDate.setText(currentDateString);

        //get current date in millis
        timeStamp = c.getTimeInMillis();

        //toolbar
        //toolbar
        toolbar = findViewById(R.id.tlbSchedule);
        ScheduleActivity.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Schedule");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //buttons
        openCalendar = findViewById(R.id.btnShowCalendar);
        addCustom = findViewById(R.id.btnAddCustom);

        openCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        addCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScheduleDialogue(false);
            }
        });

        //get schedule items
        DBManager db = new DBManager(this);
        scheduleItems = db.getScheduleItems();
        scheduleItems = getApplicable(scheduleItems);

        //recyclerview
        recyclerView = findViewById(R.id.rcySchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterSchedule = new AdapterSchedule(this, scheduleItems, this);
        recyclerView.setAdapter(adapterSchedule);

        //check if coming from the option to schedule a task
        Intent i = getIntent();
        long task_id = i.getLongExtra("task_id", 0);
        if (task_id > 0){
            task = db.getTask(task_id);
            openScheduleDialogue(false);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timeStamp = c.getTimeInMillis();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        tvDate.setText(currentDateString);
        //when picking date get all the schedule items and sort them so they fit within the date
        DBManager db = new DBManager(getApplicationContext());
        scheduleItems = db.getScheduleItems();
        scheduleItems = getApplicable(scheduleItems);
        adapterSchedule.refreshList(scheduleItems);
        adapterSchedule.notifyDataSetChanged();
    }

    public void openScheduleDialogue(boolean editing){
        builder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.schedule_popup, null);

        //dialogue edit texts
        EditText schTitle = popup.findViewById(R.id.etSetTitle);
        EditText schNote = popup.findViewById(R.id.etSetNote);
        //dialogue time setters
        setTimeStart = popup.findViewById(R.id.btnTimeStart);
        setTimeEnd = popup.findViewById(R.id.btnTimeEnd);
        //dialogue
        CheckBox checkDay = popup.findViewById(R.id.chkDay);
        CheckBox checkWeek = popup.findViewById(R.id.chkWeek);
        CheckBox checkMonth = popup.findViewById(R.id.chkMonth);
        //buttons to save or close
        Button saveNewSchItem = popup.findViewById(R.id.btnSaveDialogue);
        Button cancelNewSchItem = popup.findViewById(R.id.btnCancelDialogue);

        //if the item exists and is being edited set the text views accordingly and variables that can be preset
        if (editing){
            //get the item by the id passed on from the adapter
            DBManager db = new DBManager(this);
            scheduleItem = db.getScheduleItem(scheduleItemId);
            Log.d("in editing found ID od ->", String.valueOf(scheduleItemId));
            schTitle.setText(scheduleItem.getTitle());
            schNote.setText(scheduleItem.getNote());
            //cancel becomes the delete button
            cancelNewSchItem.setText("Delete");
            //format string
            long startTime = scheduleItem.getTimeStart();
            long endTime = scheduleItem.getTimeEnd();
            int sHour = (int) startTime/60;
            int sMin = (int) startTime%60;
            int eHour = (int) endTime/60;
            int eMin = (int) endTime%60;
            @SuppressLint("DefaultLocale") String formattedStart = String.format("%d:%02d", sHour, sMin);
            @SuppressLint("DefaultLocale") String formattedEnd = String.format("%d:%02d", eHour, eMin);
            setTimeStart.setText(formattedStart);
            setTimeEnd.setText(formattedEnd);
            //hour should be known before hand
            timeStartHour = (int) (scheduleItem.getTimeStart()/60);
            timeStartMinute = (int) (scheduleItem.getTimeStart()%60);
            timeEndHour = (int) (scheduleItem.getTimeEnd()/60);
            timeEndMinute = (int) (scheduleItem.getTimeEnd()%60);
            //check the correct box on open
            if (scheduleItem.getType() == 1){
                checkDay.setChecked(true);
            }
            else if (scheduleItem.getType() == 2){
                checkWeek.setChecked(true);
            }
            else if (scheduleItem.getType() == 3){
                checkMonth.setChecked(true);
            }
        } else { //otherwise schedule item is a new schedule item
            scheduleItem = new ScheduleItem();
        }

        //if creating a new schedule item from a task
        if(task != null){
            schTitle.setText(task.getTitle().toString().trim());
        }

        //dialogue
        builder.setView(popup);
        dialog = builder.create();
        dialog.show();

        setTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTimePicker(1);
            }
        });
        setTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTimePicker(2);
            }
        });
        saveNewSchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking fields have the correct inputs before saving/editing
                if(schTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(schTitle.getText().toString().trim().length() > 20){
                    Toast.makeText(getApplicationContext(), "Title can only be 20 letters long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(schNote.getText().toString().trim().length() > 20){
                    Toast.makeText(getApplicationContext(), "Note can only be 20 letters, intended a room name or location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(setTimeStart.getText().toString().equals("Start time") || setTimeEnd.getText().toString().equals("End time")){
                    Toast.makeText(getApplicationContext(), "Please enter a start AND end time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if((timeStartHour*60)+timeStartMinute >= (timeEndHour*60)+timeEndMinute){
                    setTimeStart.setText("Start time");
                    setTimeEnd.setText("End time");
                    Toast.makeText(getApplicationContext(), "Time start cannot be after time end", Toast.LENGTH_SHORT).show();
                    return;
                }
                //id INTEGER primary key AUTOINCREMENT NOT NULL, , date long, time_start INTEGER NOT NULL, time_end INTEGER NOT NULL, type INTEGER NOT NULL DEFAULT 0
                scheduleItem.setTitle(schTitle.getText().toString());
                scheduleItem.setNote(schNote.getText().toString());
                scheduleItem.setTimeStart((timeStartHour*60)+timeStartMinute);
                scheduleItem.setTimeEnd((timeEndHour*60)+timeEndMinute);
                if(checkDay.isChecked()){
                    scheduleItem.setType(1);
                }
                else if(checkWeek.isChecked()){
                    scheduleItem.setType(2);
                }
                else if(checkMonth.isChecked()){
                    scheduleItem.setType(3);
                }
                DBManager db = new DBManager(getApplicationContext());
                if(editing){
                    Log.d("just editing of the item ID ->", String.valueOf(scheduleItemId));
                    db.editScheduleItem(scheduleItem);
                } else {
                    //timestamp created here, we don't want this to change if we are editing it
                    Log.d("just saved the item ID ->", String.valueOf(scheduleItemId));
                    scheduleItem.setTimestamp(timeStamp);
                    db.addScheduleItem(scheduleItem);
                }
                scheduleItems = db.getScheduleItems();
                scheduleItems = getApplicable(scheduleItems);
                adapterSchedule.refreshList(scheduleItems);
                adapterSchedule.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancelNewSchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    DBManager db = new DBManager(getApplicationContext());
                    db.deleteScheduleItem(scheduleItemId);
                    scheduleItems = db.getScheduleItems();
                    scheduleItems = getApplicable(scheduleItems);
                    adapterSchedule.refreshList(scheduleItems);
                    adapterSchedule.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        checkDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWeek.setChecked(false);
                checkMonth.setChecked(false);
            }
        });
        checkWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDay.setChecked(false);
                checkMonth.setChecked(false);
            }
        });
        checkMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDay.setChecked(false);
                checkWeek.setChecked(false);
            }
        });
    }

    public void popupTimePicker(int b){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                if(b == 1) {
                    setTimeStart.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    timeStartHour = hour; timeStartMinute = minute;
                } else if (b ==2) {
                    setTimeEnd.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    timeEndHour = hour; timeEndMinute = minute;
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public List<ScheduleItem> getApplicable(List<ScheduleItem> scheduleItems){
        List<ScheduleItem> applicableList = new ArrayList<>();

            for(int i = 0 ; i < scheduleItems.size() ; i++){
                if(daysApart(scheduleItems.get(i).getTimestamp(), timeStamp).equals("same")) { //if the timestamp matches the current day
                    applicableList.add(scheduleItems.get(i));
                }

                else if(scheduleItems.get(i).getType() == 1) { //if it'not not already added, check if it repeats every day
                    applicableList.add(scheduleItems.get(i));
                }

                if(scheduleItems.get(i).getType() == 2) { //check if its a week
                    if (daysApart(scheduleItems.get(i).getTimestamp(), timeStamp).equals("week")) {
                        applicableList.add(scheduleItems.get(i));
                    }
                }
                if(scheduleItems.get(i).getType() == 3){
                    if (daysApart(scheduleItems.get(i).getTimestamp(), timeStamp).equals("month")) {
                        applicableList.add(scheduleItems.get(i));
                    }
                }

            }
        return applicableList;
    }

    public String daysApart(long dateOne, long dateTwo){
        //get current day
        Calendar date1 = Calendar.getInstance();
        date1.setTimeInMillis((long) Math.floor(dateOne/86400000)*86400000);
        //get day to compare
        Calendar date2 = Calendar.getInstance();
        date2.setTimeInMillis((long) Math.floor(dateTwo/86400000)*86400000);
        //check how many days are apart
        long days = ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant());
        if (days == 0){
            return "same";
        }
        if (days % 28 == 0){
            return "month";
        }
        if (days % 7 == 0){
            return "week";
        }
        else {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void EditScheduleItem(long schedule_item_id) {
        Log.d("editing of the item ID ->", String.valueOf(schedule_item_id));
        this.scheduleItemId = schedule_item_id;
        this.editing = true;
        openScheduleDialogue(true);
    }
}