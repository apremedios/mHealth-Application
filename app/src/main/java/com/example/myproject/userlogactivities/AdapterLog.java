package com.example.myproject.userlogactivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;

import java.util.List;

public class AdapterLog extends RecyclerView.Adapter<AdapterLog.ViewHolder> {

    private LayoutInflater inflater;
    private List<LogItem> logItems;

    AdapterLog(Context context, List<LogItem> logItems) {
        this.inflater = LayoutInflater.from(context);
        this.logItems = logItems;
    };

    @NonNull
    @Override
    public AdapterLog.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_log_view, viewGroup,false);

        return new AdapterLog.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLog.ViewHolder viewHolder, int i) {
        String title = logItems.get(i).getTitle();
        String date = logItems.get(i).getDate();
        String time = logItems.get(i).getTime();
        viewHolder.tvTitle.setText(title);
        viewHolder.tvDateTime.setText("Completed: " + date + " " + "At: " + time);

        String type = logItems.get(i).getThing_type();
        if(type.equals("task")){
            viewHolder.icon.setImageResource(R.drawable.ic_task_complete);
        } else if (type.equals("goal")){
            viewHolder.icon.setImageResource(R.drawable.ic_trophy);
        }

    }

    @Override
    public int getItemCount() {
        return logItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvDateTime;
        ImageView icon;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            icon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    public void refreshList(List<LogItem> logItems){
        this.logItems = logItems;
        notifyDataSetChanged();
    }
}