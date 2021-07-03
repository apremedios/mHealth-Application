package com.example.myproject.scheduleactivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.AppListener;
import com.example.myproject.R;

import java.util.List;

public class AdapterSchedule extends RecyclerView.Adapter<AdapterSchedule.ViewHolder> {

    private LayoutInflater inflater;
    private List<ScheduleItem> scheduleItems;
    private AppListener listener;

    AdapterSchedule(Context context, List<ScheduleItem> scheduleItems, AppListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.scheduleItems = scheduleItems;
        this.listener = listener;
    };

    @NonNull
    @Override
    public AdapterSchedule.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_schedule_view, viewGroup,false);

        return new AdapterSchedule.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSchedule.ViewHolder viewHolder, int i) {
        String title = scheduleItems.get(i).getTitle();
        //format string
        long startTime = scheduleItems.get(i).getTimeStart();
        long endTime = scheduleItems.get(i).getTimeEnd();
        int sHour = (int) startTime/60;
        int sMin = (int) startTime%60;
        int eHour = (int) endTime/60;
        int eMin = (int) endTime%60;
        @SuppressLint("DefaultLocale") String formattedStart = String.format("%d:%02d", sHour, sMin);
        @SuppressLint("DefaultLocale") String formattedEnd = String.format("%d:%02d", eHour, eMin);
        //set item texts
        viewHolder.tvTitle.setText(title);
        viewHolder.tvTimes.setText(formattedStart + " - " + formattedEnd);

    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvNote, tvTimes;
        Button editScheduleItem;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            //textview
            tvTitle = itemView.findViewById(R.id.tvItemTitle);
            tvTimes = itemView.findViewById(R.id.tvItemTime);
            //buttons
            editScheduleItem = itemView.findViewById(R.id.btnEditScheduleItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            editScheduleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.EditScheduleItem(scheduleItems.get(getAdapterPosition()).getID());
                }
            });
        }

    }

    public void refreshList(List<ScheduleItem> scheduleItems){
        this.scheduleItems = scheduleItems;
        notifyDataSetChanged();
    }
}