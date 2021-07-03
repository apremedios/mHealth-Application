package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myproject.fragments.ActivitiesFragment;
import com.example.myproject.fragments.DietFragment;
import com.example.myproject.fragments.FitnessFragment;
import com.example.myproject.fragments.PlannerFragment;
import com.example.myproject.fragments.TodayFragment;
import com.example.myproject.goalactivities.Goal;
import com.example.myproject.scheduleactivities.ScheduleActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //on init, create the first goal which holds custom tasks
        DBManager db = new DBManager(this);
        boolean holderGoalExists = db.holderGoalExists();
        if(!holderGoalExists){
            Goal goal = new Goal("custom", "", "", "");
            db.addGoal(goal);
        }

        ImageButton OptionsMenu = (ImageButton)findViewById(R.id.btnOptions);
        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.ic_planner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper,
                new PlannerFragment()).commit();

        OptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, OptionsActivity.class));
            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.ic_activities:
                        selectedFragment = new ActivitiesFragment();
                        break;
                    case R.id.ic_today:
                        startActivity(new Intent(SecondActivity.this, ScheduleActivity.class));
                        return true;
                    case R.id.ic_planner:
                        selectedFragment = new PlannerFragment();
                        break;
                    case R.id.ic_fitness:
                        selectedFragment = new FitnessFragment();
                        break;
                    case R.id.ic_diet:
                        selectedFragment = new DietFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper,
                        selectedFragment).commit();
                return true;
            }
        });

    }
}

