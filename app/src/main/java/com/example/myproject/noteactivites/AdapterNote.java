package com.example.myproject.noteactivites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myproject.R;

import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Note> notes;

    AdapterNote(Context context, List<Note> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_note_view, viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title = notes.get(i).getTitle();
        String date = notes.get(i).getDate();
        String time = notes.get(i).getTime();
        viewHolder.tvTitle.setText(title);
        viewHolder.tvDate.setText("Created: " + date);
        viewHolder.tvTime.setText("at: " + time);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvDate, tvTime;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvPercent);
            tvDate = itemView.findViewById(R.id.tvDateTime);
            tvTime = itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ShowNote.class);
                    i.putExtra("id", notes.get(getAdapterPosition()).getID());
                    v.getContext().startActivity(i);
                }
            });
        }

    }

}
