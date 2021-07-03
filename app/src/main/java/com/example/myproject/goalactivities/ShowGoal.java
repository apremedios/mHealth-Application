package com.example.myproject.goalactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.noteactivites.AdapterNote;
import com.example.myproject.noteactivites.Note;

import org.w3c.dom.Text;

import java.util.List;

public class ShowGoal extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Task> tasks;
    Goal goal;
    boolean editing = false;
    EditText GoalTitle, Motivation;
    Button AddNewTask, CompleteGoal;
    AdapterTasks adapterTasks;
    Toolbar toolbar;
    TextView tvInRcy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goal);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //database
        DBManager db = new DBManager(this);

        //edit texts
        GoalTitle = findViewById(R.id.etGoalTitle);
        Motivation = findViewById(R.id.etMotivation);
        //buttons
        AddNewTask = findViewById(R.id.btnAddNewTask);
        CompleteGoal = findViewById(R.id.btnCompleteGoal);


        //get intent and id from selection...
        Intent i = getIntent();
        Long id = i.getLongExtra("id", 0);
        //...then get goal
        goal = db.getGoal(id);

        //tasks belonging to goal
        tasks = db.getTasks(goal.getID());
        //text view
        tvInRcy = findViewById(R.id.tvRcyTasks);
        if(tasks.isEmpty()){
            tvInRcy.setAlpha(1.0f);
        }else{
            tvInRcy.setAlpha(0.0f);
        }
        //... and set edit texts to corresponding goal
        GoalTitle.setText(goal.getTitle());
        GoalTitle.setEnabled(false);
        GoalTitle.setTextColor(Color.BLACK);
        Motivation.setText(goal.getMotivation());
        Motivation.setEnabled(false);
        Motivation.setTextColor(Color.BLACK);

        //toolbar
        toolbar = findViewById(R.id.tlbGoals);
        ShowGoal.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(GoalTitle.getText().toString().trim());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //recyclerview
        recyclerView = findViewById(R.id.rcyGoals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterTasks = new AdapterTasks(this, tasks, false);
        recyclerView.setAdapter(adapterTasks);

        AddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ShowTask.class);
                i.putExtra("goal_id", goal.getID());
                v.getContext().startActivity(i);
            }
        });
        CompleteGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.setGoalComplete(goal, 1);
                startActivity(new Intent(ShowGoal.this, GoalsActivity.class));
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_goal_menu, menu);
        return true;
    }*/

    //changed from default (commented out above) in order to change menu items dynamically
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();

        if(!editing){
        inflater.inflate(R.menu.show_goal_menu, menu);
        } else {
            inflater.inflate(R.menu.edit_goal_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //call database for use
        DBManager db = new DBManager(this);

        if(item.getItemId() == R.id.editGoal){
            Toast.makeText(this, "Edit Goal", Toast.LENGTH_SHORT).show();
            //enable editing
            GoalTitle.setEnabled(true);
            Motivation.setEnabled(true);
            editing = true;
            //change the menu by invalidating it - by default the 'onPrepareOptionsMenu' is called
            invalidateOptionsMenu();
        }
        if(item.getItemId() == R.id.deleteGoal){
            Toast.makeText(this, "Delete Goal", Toast.LENGTH_SHORT).show();
            db.deleteGoal(goal.getID());
            startActivity(new Intent(ShowGoal.this, GoalsActivity.class));
        }
        if(item.getItemId() == R.id.saveGoal){
            Toast.makeText(this, "Goal Saved", Toast.LENGTH_SHORT).show();
            //save the edited goal to the database
            goal.setTitle(GoalTitle.getText().toString());
            goal.setMotivation(Motivation.getText().toString());
            int id = db.editGoal(goal);
            //set editing to false and reload corresponding menu
            GoalTitle.setEnabled(false);
            Motivation.setEnabled(false);
            editing = false;
            invalidateOptionsMenu();
        }
        Log.d("is it true? > ", String.valueOf(editing));
        if(item.getItemId() == R.id.cancelGoalEdit){
            Toast.makeText(this, "Edit Cancelled", Toast.LENGTH_SHORT).show();
            //set editing to false discard changes and reload corresponding menu
            GoalTitle.setEnabled(false);
            GoalTitle.setText(goal.getTitle());
            Motivation.setEnabled(false);
            Motivation.setText(goal.getMotivation());
            editing = false;
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShowGoal.this, GoalsActivity.class));
        super.onBackPressed();
    }

}