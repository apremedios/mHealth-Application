package com.example.myproject.goalactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.TimerActivity;
import com.example.myproject.mytasks.TaskViewer;
import com.example.myproject.scheduleactivities.ScheduleActivity;

import java.util.List;

public class ShowTask extends AppCompatActivity {

    RecyclerView recyclerView;
    Task task;
    long goal_id;
    long task_id;
    boolean editing = false;
    boolean in_task_viewer;
    EditText TaskTitle, TaskNotes, CheckListField;
    Button AddCheck, AddToSchedule, GoToTimeTask;
    Toolbar toolbar;
    AdapterCheckList adapterCheckList;
    List<Check> checkList;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //database
        DBManager db = new DBManager(this);

        //edit texts
        TaskTitle = findViewById(R.id.etTaskTitle);
        TaskNotes = findViewById(R.id.etTaskNotes);
        CheckListField = findViewById(R.id.etAddToChecklist);
        TaskTitle.setTextColor(Color.BLACK);
        TaskTitle.setBackgroundResource(R.drawable.white_round);
        TaskNotes.setTextColor(Color.BLACK);
        TaskNotes.setBackgroundResource(R.drawable.white_round);
        CheckListField.setTextColor(Color.BLACK);
        CheckListField.setBackgroundResource(R.drawable.white_round);


        //buttons
        AddCheck = findViewById(R.id.btnAddCheck);
        AddToSchedule = findViewById(R.id.btnTaskSchedule);
        GoToTimeTask = findViewById(R.id.btnGoTimer);

        //toolbar
        toolbar = findViewById(R.id.tlbTasks);
        ShowTask.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Task");

        //get previous intent instance
        Intent i = getIntent();
        goal_id = i.getLongExtra("goal_id", 0);
        //get task id
        task_id = i.getLongExtra("task_id", 0); //might need to make it less than 0, in case the minimum table index is 0
        //check if coming from task viewer
        in_task_viewer = i.getBooleanExtra("in_task_viewer", false);

        //if task id <=0 then treat it as a new task to be created
        if (task_id <= 0) {
            editing = true;
            getSupportActionBar().setTitle("New Task");
            CheckListField.setEnabled(false);
            CheckListField.setText("Save first to add checks");
            CheckListField.setBackgroundColor(R.color.grey_out);
            AddCheck.setEnabled(false);
            AddToSchedule.setEnabled(false);
            GoToTimeTask.setEnabled(false);
        } else { //otherwise task does not need to be edited or
            task = db.getTask(task_id);
            getSupportActionBar().setTitle(task.getTitle());
            TaskTitle.setText(task.getTitle());
            TaskNotes.setText(task.getNotes());
            TaskTitle.setEnabled(false);
            TaskNotes.setEnabled(false);
        }

        //get check list
        checkList = db.getCheckList(task_id);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //recyclerView
        recyclerView = findViewById(R.id.rcyCheckList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterCheckList = new AdapterCheckList(this, checkList);
        recyclerView.setAdapter(adapterCheckList);
        recyclerView.setBackgroundResource(R.drawable.white_round);

        //touch helper
        ItemTouchHelper touchHelper = new ItemTouchHelper(new CheckItemTouchHelper(adapterCheckList));
        touchHelper.attachToRecyclerView(recyclerView);

        //adding to checklist, update list and notify adapter
        AddCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check check = new Check(task_id, CheckListField.getText().toString());
                db.addCheck(check, task_id);
                checkList = db.getCheckList(task_id);
                CheckListField.setText("");
                adapterCheckList.refreshList(checkList);
                adapterCheckList.notifyDataSetChanged();
            }
        });
        AddToSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowTask.this, ScheduleActivity.class);
                i.putExtra("task_id", task_id);
                startActivity(new Intent(i));
            }
        });
        GoToTimeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowTask.this, TimerActivity.class);
                i.putExtra("task_id", task_id);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //call database for use
        DBManager db = new DBManager(this);

        if(task_id <= 0){ // if it's a new task, do this
            if(item.getItemId() == R.id.saveTask){
                Task task = new Task(goal_id, TaskTitle.getText().toString(), TaskNotes.getText().toString());
                db.addTask(task, goal_id);
                onBackPressed();

            }
            if(item.getItemId() == R.id.cancelTaskEdit){
                onBackPressed();
            }
        } else { //if its already a created task, do this instead
            if(item.getItemId() == R.id.editTask){
                Toast.makeText(this, "Edit Goal", Toast.LENGTH_SHORT).show();
                //enable editing
                TaskTitle.setEnabled(true);
                TaskNotes.setEnabled(true);
                CheckListField.setEnabled(false);
                AddCheck.setEnabled(false);
                editing = true;
                //change the menu by invalidating it - by default the 'onPrepareOptionsMenu' is called
                invalidateOptionsMenu();
            }
            if(item.getItemId() == R.id.deleteTask){
                Toast.makeText(this, "Deleted Task", Toast.LENGTH_SHORT).show();
                db.deleteTask(task.getID());
                onBackPressed();
            }
            if(item.getItemId() == R.id.saveTask){
                Toast.makeText(this, "Goal Saved", Toast.LENGTH_SHORT).show();
                //save the edited goal to the database
                task.setTitle(TaskTitle.getText().toString());
                task.setNotes(TaskNotes.getText().toString());
                int id = db.editTask(task);
                //set editing to false and reload corresponding menu
                TaskTitle.setEnabled(false);
                TaskNotes.setEnabled(false);
                CheckListField.setEnabled(true);
                AddCheck.setEnabled(true);
                editing = false;
                invalidateOptionsMenu();

            }
            if(item.getItemId() == R.id.cancelTaskEdit){
                Toast.makeText(this, "Edit Cancelled", Toast.LENGTH_SHORT).show();
                //set editing to false discard changes and reload corresponding menu
                TaskTitle.setEnabled(false);
                TaskTitle.setText(task.getTitle());
                TaskNotes.setEnabled(false);
                TaskNotes.setText(task.getNotes());
                CheckListField.setEnabled(true);
                AddCheck.setEnabled(true);
                editing = false;
                invalidateOptionsMenu();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i;
        if(in_task_viewer){
            i = new Intent(ShowTask.this, TaskViewer.class);
        }else {
            i = new Intent(ShowTask.this, ShowGoal.class);
            i.putExtra("id", goal_id);
        }
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();

        if(!editing){
            inflater.inflate(R.menu.show_task_menu, menu);
        } else {
            inflater.inflate(R.menu.edit_task_menu, menu);
        }
        return true;
    }
}