package com.example.myproject.goalactivities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;

import java.util.List;

public class AdapterGoals extends RecyclerView.Adapter<AdapterGoals.ViewHolder> {

    private LayoutInflater inflater;
    private List<Goal> goals;

    AdapterGoals(Context context, List<Goal> goals) {
        this.inflater = LayoutInflater.from(context);
        this.goals = goals;
    };

    @NonNull
    @Override
    public AdapterGoals.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_goal_view, viewGroup,false);

        return new AdapterGoals.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGoals.ViewHolder viewHolder, int i) {
        String title = goals.get(i).getTitle();
        String date = goals.get(i).getDate();
        String time = goals.get(i).getTime();
        viewHolder.tvGoalTitle.setText(title);
        viewHolder.tvGoalDate.setText(date);
        viewHolder.tvGoalTime.setText(time);

    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvGoalTitle;
        TextView tvGoalDate;
        TextView tvGoalTime;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            tvGoalTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvGoalDate = itemView.findViewById(R.id.tvDateTime);
            tvGoalTime = itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ShowGoal.class);
                    i.putExtra("id", goals.get(getAdapterPosition()).getID());
                    v.getContext().startActivity(i);
                }
            });
        }

    }

    public void refreshList(List<Goal> goals){
        this.goals = goals;
        notifyDataSetChanged();
    }
}
