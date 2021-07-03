package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.goalactivities.Task;
import com.example.myproject.noteactivites.NotesActivity;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean running;
    private TextView TaskTitle, tvCountdown;
    private Button startPause, finish;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Task task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //countdown
        tvCountdown = findViewById(R.id.tvCountDown);

        //buttons
        startPause = findViewById(R.id.btnStartPause);
        finish = findViewById(R.id.btnFinish);
        //text views
        TaskTitle = findViewById(R.id.tvTimerTaskTitle);

        //get extra from showActivity
        Intent i = getIntent();
        long id = i.getLongExtra("task_id", 0);

        //get associated task and set associated items in view
        DBManager db = new DBManager(this);
        task = db.getTask(id);
        TaskTitle.setText(task.getTitle());

        //toolbar
        toolbar = findViewById(R.id.tlbTimer);
        TimerActivity.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(task.getTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    pauseTimer();
                }
                else {
                    if(timeLeftInMillis <= 0){
                    openTimePickerDialogue();
                    } else {
                        startTimer();
                    }
                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTimer();
            }
        });

        updateCountdownText();
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                running = false;
                startPause.setText("start");
                startPause.setVisibility(View.INVISIBLE);
                finish.setVisibility(View.VISIBLE);
                Toast.makeText(TimerActivity.this, "done!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        running = true;
        startPause.setText("pause");
        finish.setVisibility(View.INVISIBLE);
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        running = false;
        startPause.setText("start");
        finish.setVisibility(View.VISIBLE);
    }

    public void finishTimer(){
        timeLeftInMillis = 0;
        updateCountdownText();
        finish.setVisibility(View.VISIBLE);
        startPause.setVisibility(View.VISIBLE);
        setTaskOptions();
    }

    private void updateCountdownText(){
        int minutes = (int) timeLeftInMillis/1000/60;
        int seconds = (int) timeLeftInMillis/1000%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        tvCountdown.setText(timeLeftFormatted);
    }

    private void openTimePickerDialogue(){
        builder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.time_pick_popup, null);

        //buttons
        Button setTimeAndStart = popup.findViewById(R.id.btnSelectTime);

        //number pickers
        NumberPicker npHours = popup.findViewById(R.id.numPickHour);
        npHours.setMinValue(0);
        npHours.setMaxValue(6);
        NumberPicker npMin = popup.findViewById(R.id.numPickMin);
        npMin.setMinValue(0);
        npMin.setMaxValue(59);

        //show dialogue
        builder.setView(popup);
        dialog = builder.create();
        dialog.show();

        setTimeAndStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLeftInMillis = npHours.getValue()*3600000 + npMin.getValue()*60000;
                //added this to check
                if (timeLeftInMillis <= 0){
                    setTaskOptions();
                } else {
                    dialog.dismiss();
                    startTimer();
                }
            }
        });
    }

    private void setTaskOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Timer finished!");
        builder.setMessage("Would you like to set the task as complete?");
        builder.setPositiveButton("Complete task",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager db = new DBManager(getApplicationContext());
                        db.setTaskComplete(task, 1);
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}