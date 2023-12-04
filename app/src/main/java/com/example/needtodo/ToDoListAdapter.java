package com.example.needtodo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private List<ToDoList> mToDoList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thingImage;
        TextView thing;
        TextView deadline;
        View todolistView;
        LinearLayout linearLayout;

        public  ViewHolder(View view){
            super(view);
            linearLayout = view.findViewById(R.id.listSquare);
            todolistView = view;
            thing = view.findViewById(R.id.todolist_name);
            thingImage = view.findViewById(R.id.todolist_image);
            deadline = view.findViewById(R.id.todolist_deadline);
        }
    }
    public ToDoListAdapter(List<ToDoList> toDoListList){
        mToDoList = toDoListList;
    }
    @NonNull
    @Override
    public ToDoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.todolistView.setOnClickListener(v -> {
            Context context = parent.getContext();
            int position = holder.getAbsoluteAdapterPosition();
            ToDoList toDoList = mToDoList.get(position);
            Intent intent = new Intent(context, QueryAndChange.class);
            intent.putExtra("content",toDoList);
            context.startActivity(intent);
        });
        holder.todolistView.setOnLongClickListener(v -> {
            Context context = parent.getContext();
            int position = holder.getAbsoluteAdapterPosition();
            ToDoList toDoList = mToDoList.get(position);
            Intent intent = new Intent(context, TopAndDone.class);
            intent.putExtra("content",toDoList);
            context.startActivity(intent);
            return true;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListAdapter.ViewHolder holder, int position) {
        ViewHolder viewHolder = holder;
        ToDoList toDoList = mToDoList.get(position);
        holder.thingImage.setImageResource(toDoList.getImageID());
        holder.thing.setText(toDoList.getThing());
        holder.deadline.setText(toDoList.getDeadline());
        holder.linearLayout.setBackgroundColor(Color.parseColor(toDoList.getBackgroundColor()));
    }

    @Override
    public int getItemCount() {
        return mToDoList.size();
    }

}
