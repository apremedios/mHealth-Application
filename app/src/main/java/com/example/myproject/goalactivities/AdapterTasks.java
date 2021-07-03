package com.example.myproject.goalactivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.DBManager;
import com.example.myproject.R;

import java.util.List;

public class AdapterTasks extends RecyclerView.Adapter<AdapterTasks.ViewHolder> {

    private LayoutInflater inflater;
    private List<Task> tasks;
    private boolean in_task_viewer;

    public AdapterTasks(Context context, List<Task> tasks, boolean in_task_viewer) {
        this.inflater = LayoutInflater.from(context);
        this.tasks = tasks;
        this.in_task_viewer = in_task_viewer;
    };

    @NonNull
    @Override
    public AdapterTasks.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_task_view, viewGroup,false);

        return new AdapterTasks.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AdapterTasks.ViewHolder viewHolder, int i) {
        //get title and display
        String title = tasks.get(i).getTitle();
        viewHolder.tvTaskTitle.setText(title);
        //change colour depending on completion
        if(tasks.get(i).getComplete() == 0){
            viewHolder.taskHolder.setCardBackgroundColor(Color.parseColor("#FF8686"));
            viewHolder.btnTaskComplete.setImageResource(R.drawable.ic_baseline_check);
        } else {
            viewHolder.taskHolder.setCardBackgroundColor(Color.parseColor("#A5Fd92"));
            viewHolder.btnTaskComplete.setImageResource(R.drawable.ic_baseline_cancel);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTaskTitle;
        CardView taskHolder;
        ImageButton btnTaskComplete, btnEditTask;//, btnTaskDelete;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            //database
            DBManager db = new DBManager(itemView.getContext());
            //task title
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            //task background
            taskHolder = itemView.findViewById(R.id.taskHolder);
            //task card buttons
            btnTaskComplete = itemView.findViewById(R.id.btnCompleteTask);
            btnTaskComplete.setBackgroundResource(R.drawable.purple_round);
            btnEditTask = itemView.findViewById(R.id.btnEditTask);
            btnEditTask.setBackgroundResource(R.drawable.purple_round);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btnTaskComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tasks.get(getAdapterPosition()).getComplete() == 1){
                        db.setTaskComplete(tasks.get(getAdapterPosition()), 0);
                    } else {
                        db.setTaskComplete(tasks.get(getAdapterPosition()), 1);
                    }
                    //if in task viewer
                    if (in_task_viewer){
                        tasks = db.getAllTasks();
                    }else {
                        tasks = db.getTasks(tasks.get(getAdapterPosition()).getGoal_id());
                    }
                    notifyDataSetChanged();
                }
            });
            btnEditTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ShowTask.class);
                    i.putExtra("goal_id", tasks.get(getAdapterPosition()).getGoal_id());
                    i.putExtra("task_id", tasks.get(getAdapterPosition()).getID());
                    i.putExtra("in_task_viewer", in_task_viewer);
                    v.getContext().startActivity(i);
                }
            });
        }

    }
}
