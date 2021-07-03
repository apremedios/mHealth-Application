package com.example.myproject.userlogactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.SecondActivity;
import com.example.myproject.goalactivities.AdapterGoals;
import com.example.myproject.goalactivities.Goal;
import com.example.myproject.goalactivities.GoalsActivity;

import java.util.List;

public class LogActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterLog adapterLog;
    TextView tvInRyc;
    List<LogItem> logItems;
    Button InProgress, Complete, AddNewGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //toolbar
        toolbar = findViewById(R.id.tlbLog);
        LogActivity.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Log");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get log items list
        DBManager db = new DBManager(this);
        logItems = db.getLogItems();

        //recyclerview
        recyclerView = findViewById(R.id.rcyLog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterLog = new AdapterLog(this, logItems);
        recyclerView.setAdapter(adapterLog);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LogActivity.this, SecondActivity.class));
        super.onBackPressed();
    }
}