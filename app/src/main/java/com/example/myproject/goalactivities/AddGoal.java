package com.example.myproject.goalactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.SecondActivity;
import com.example.myproject.UsefulFunctions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AddGoal extends AppCompatActivity {

    Toolbar toolbar;
    EditText GoalTitle, Motivation;
    Button AddGoal, ClearGoal, Done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //toolbar
        toolbar = findViewById(R.id.tlbGoal);
        AddGoal.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Some of Your Goals");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //edit text
        GoalTitle = findViewById(R.id.etGoalTitle);
        Motivation = findViewById(R.id.etMotivation);

        //buttons
        AddGoal = findViewById(R.id.btnAddGoal);
        ClearGoal = findViewById(R.id.btnClearGoal);
        Done = findViewById(R.id.btnDone);

        AddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoalTitle.getText().toString().trim().isEmpty() || Motivation.getText().toString().trim().isEmpty()){
                    Toast.makeText(AddGoal.this, "Please enter a Title and Motivation", Toast.LENGTH_SHORT).show();
                } else {
                    Goal goal = new Goal(GoalTitle.getText().toString(), Motivation.getText().toString(), UsefulFunctions.getCurrentDate(), UsefulFunctions.getCurrentTime());
                    DBManager db = new DBManager(AddGoal.this);
                    db.addGoal(goal);
                    GoalTitle.setText("");
                    Motivation.setText("");
                }
            }
        });

        ClearGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear current text and reset hints
                GoalTitle.setText("");
                Motivation.setText("");
            }
        });
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddGoal.this, GoalsActivity.class));
        super.onBackPressed();
    }

}