package com.example.myproject.goalactivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.DBManager;
import com.example.myproject.R;
import com.example.myproject.SecondActivity;

import java.util.List;

public class AdapterCheckList extends RecyclerView.Adapter<AdapterCheckList.ViewHolder> {

    private LayoutInflater inflater;
    private List<Check> checkList;
    public Context context;

    AdapterCheckList(Context context, List<Check> checkList) {
        this.inflater = LayoutInflater.from(context);
        this.checkList = checkList;
        this.context = context;
    };

    @NonNull
    @Override
    public AdapterCheckList.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_checklist_view, viewGroup,false);

        return new AdapterCheckList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCheckList.ViewHolder viewHolder, int i) {
        //get title and display
        String title = checkList.get(i).getTitle();
        viewHolder.todoCheckBox.setText(title);
        if(checkList.get(i).getComplete() == 1){
            viewHolder.todoCheckBox.setChecked(true);
        } else {
            viewHolder.todoCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() { return checkList.size(); }

    public final Context getContext(){return context;}

    public void deleteItem(int i){
        Check check = checkList.get(i);
        DBManager db = new DBManager(context);
        db.deleteCheck(check.getID());
        checkList.remove(i);
        notifyItemRemoved(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox todoCheckBox;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            //database
            DBManager db = new DBManager(itemView.getContext());
            //checkbox
            todoCheckBox = itemView.findViewById(R.id.todoCheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            todoCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (todoCheckBox.isChecked()) {
                        Check check = checkList.get(getAdapterPosition());
                        db.setCheckComplete(check, 1);
                    } else if (!todoCheckBox.isChecked()){
                        Check check = checkList.get(getAdapterPosition());
                        db.setCheckComplete(check, 0);
                    }
                    checkList = db.getCheckList(checkList.get(getAdapterPosition()).getTask_id());
                }
            });
        }
    }

    //for notifying and updating in other classes
    public void refreshList(List<Check> checklist){
        this.checkList = checklist;
        notifyDataSetChanged();
    }
}
