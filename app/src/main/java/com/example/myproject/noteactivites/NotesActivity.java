package com.example.myproject.noteactivites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.SecondActivity;

import java.util.List;

public class NotesActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterNote adapterNote;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //toolbar
        toolbar = findViewById(R.id.tlbNotes);
        NotesActivity.this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Notes");

        //get notes from database
        DBManager db = new DBManager(this);
        notes = db.getNotes();

        //recyclerview
        recyclerView = findViewById(R.id.rcyNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterNote = new AdapterNote(this, notes);
        recyclerView.setAdapter(adapterNote);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addNotes){
            startActivity(new Intent(NotesActivity.this, AddNote.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NotesActivity.this, SecondActivity.class));
        super.onBackPressed();
    }
}