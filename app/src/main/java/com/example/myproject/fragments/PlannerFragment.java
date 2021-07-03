package com.example.myproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myproject.DBManager;
import com.example.myproject.goalactivities.Goal;
import com.example.myproject.goalactivities.GoalsActivity;
import com.example.myproject.mytasks.TaskViewer;
import com.example.myproject.noteactivites.NotesActivity;
import com.example.myproject.R;
import com.example.myproject.scheduleactivities.ScheduleActivity;
import com.example.myproject.userlogactivities.LogActivity;

public class PlannerFragment extends Fragment {

    View view;
    Button Today, MyNotes, Goals, MyCalendar, MyLog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_planner, container, false);

        //buttons
        Button Today = view.findViewById(R.id.btnTodaysTasks);
        Button Goals = view.findViewById(R.id.btnMyGoals);
        Button MyTasks = view.findViewById(R.id.btnMyTasks);
        Button MyNotes = view.findViewById(R.id.btnMyNotes);
        Button MyLog = view.findViewById(R.id.btnMyLog);

        Goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GoalsActivity.class));
            }
        });

        MyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotesActivity.class));
            }
        });

        MyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LogActivity.class));
            }
        });

        Today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScheduleActivity.class));
            }
        });
        MyTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TaskViewer.class));
            }
        });

        return view;
    }
}
