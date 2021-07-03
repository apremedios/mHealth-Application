package com.example.myproject.goalactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.SecondActivity;
import com.example.myproject.noteactivites.AdapterNote;
import com.example.myproject.noteactivites.Note;
import com.example.myproject.noteactivites.NotesActivity;

import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterGoals adapterGoals;
    TextView tvInRyc;
    List<Goal> goals;
    Button InProgress, Complete, AddNewGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //buttons
        InProgress = findViewById(R.id.btnInProgress);
        Complete = findViewById(R.id.btnComplete);
        AddNewGoal = findViewById(R.id.btnAddGoal);

        //text view
        tvInRyc = findViewById(R.id.tvRcyGoals);
        tvInRyc.setAlpha(0.0f);
        //toolbar
        toolbar = findViewById(R.id.tlbNotes);
        GoalsActivity.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Goals");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get goals from database
        DBManager db = new DBManager(this);
        goals = db.getIncompleteGoals();
        if(goals.isEmpty()){
            tvInRyc.setAlpha(1.0f);
        }

        //recyclerview
        recyclerView = findViewById(R.id.rcyGoals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterGoals = new AdapterGoals(this, goals);
        recyclerView.setAdapter(adapterGoals);

        AddNewGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoalsActivity.this, AddGoal.class));
            }
        });
        InProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goals = db.getIncompleteGoals();
                adapterGoals.refreshList(goals);
                tvInRyc.setAlpha(0.0f);
                if(goals.isEmpty()) {
                    tvInRyc.setAlpha(1.0f);
                    tvInRyc.setText("Add a Goal!");
                }
            }
        });
        Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goals = db.getCompleteGoals();
                adapterGoals.refreshList(goals);
                tvInRyc.setAlpha(0.0f);
                if(goals.isEmpty()) {
                    tvInRyc.setAlpha(1.0f);
                    tvInRyc.setText("Let's get a Goal Done!");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GoalsActivity.this, SecondActivity.class));
        super.onBackPressed();
    }
}