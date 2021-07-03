package com.example.myproject.noteactivites;

import android.content.Intent;
import android.os.Bundle;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ShowNote extends AppCompatActivity {

    TextView tvContent;
    Toolbar toolbar;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //text views
        tvContent = findViewById(R.id.tvContent);

        //get intent and id from selection
        Intent i = getIntent();
        Long id = i.getLongExtra("id", 0);
        DBManager db = new DBManager(this);
        note = db.getNote(id);

        //set items according to corresponding note
        getSupportActionBar().setTitle(note.getTitle());
        tvContent.setText(note.getText());

        //allow scrolling in the content
        tvContent.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteNote(note.getID());
                Toast.makeText(getApplicationContext(), "Note" + " '" + note.getTitle() + "' "+ "deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), NotesActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnEditNote){
            //go to edit note activity and pass id to the next intent
            Intent i = new Intent(this, EditNote.class);
            i.putExtra("id", note.getID());
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, NotesActivity.class));
        super.onBackPressed();
    }

}