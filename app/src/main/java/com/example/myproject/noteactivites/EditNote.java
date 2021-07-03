package com.example.myproject.noteactivites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myproject.DBManager;
import com.example.myproject.R;

import java.util.Calendar;

public class EditNote extends AppCompatActivity {

    Toolbar toolbar;
    EditText noteTitle, noteText;
    Calendar calendar;
    String currentDate;
    String currentTime;
    Note note;
    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent i = getIntent();
        Long id = i.getLongExtra("id", 0);
        db = new DBManager(this);
        note = db.getNote(id);

        toolbar = findViewById(R.id.tlbNotes);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) EditNote.this).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(note.getTitle());

        noteTitle = findViewById(R.id.etTitle);
        noteText = findViewById(R.id.etText);
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getText());

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() !=0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get current date and time
        calendar =  Calendar.getInstance();
        currentDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));

    }

    private String pad(int i) {
        if(i<10){
            return "0" + i;
        }
        return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addnote_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteNote){
            onBackPressed();
        }
        if(item.getItemId() == R.id.saveNote){
            note.setTitle(noteTitle.getText().toString());
            note.setText(noteText.getText().toString());
            int id = db.editNote(note);
            if(id == note.getID()){
                Toast.makeText(getApplicationContext(), "Note" + " '" + note.getTitle() + "' "+ "updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Note" + " '" + note.getTitle() + "' "+ "error in updating", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(getApplicationContext(), ShowNote.class);
            i.putExtra("id", note.getID());
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}