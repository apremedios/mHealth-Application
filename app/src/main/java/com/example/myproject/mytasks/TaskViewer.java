package com.example.myproject.mytasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.SecondActivity;
import com.example.myproject.goalactivities.AdapterTasks;
import com.example.myproject.goalactivities.Goal;
import com.example.myproject.goalactivities.ShowTask;
import com.example.myproject.goalactivities.Task;

import java.util.List;

public class TaskViewer extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterTasks adapterTasks;
    List<Task> allTasks;
    Button AddCustomTask;
    Goal goal; //the un-editable goal which holds custom tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_viewer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //buttons
        AddCustomTask = findViewById(R.id.btnAddCustomTask);

        //database
        DBManager db = new DBManager(this);

        //test
        goal = db.getHolderGoal();

        //get all tasks
        allTasks = db.getAllTasks();

        //toolbar
        toolbar = findViewById(R.id.tlbMyTasks);
        TaskViewer.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Tasks");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //recyclerview
        recyclerView = findViewById(R.id.rctMyTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterTasks = new AdapterTasks(this, allTasks, true);
        recyclerView.setAdapter(adapterTasks);

        AddCustomTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TaskViewer.this, ShowTask.class);
                i.putExtra("goal_id", goal.getID());
                i.putExtra("in_task_viewer", true);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TaskViewer.this, SecondActivity.class));
        super.onBackPressed();
    }
}