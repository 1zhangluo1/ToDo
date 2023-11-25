package com.example.needtodo;

import android.content.Context;
import android.content.Intent;
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
        LinearLayout listSquare;
        ImageView thingImage;
        TextView thing;
        TextView deadline;
        View todolistView;

        public  ViewHolder(View view){
            super(view);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.listSquare);
            todolistView = view;
            thing = (TextView) view.findViewById(R.id.todolist_name);
            thingImage = (ImageView) view.findViewById(R.id.todolist_image);
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
        holder.todolistView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = parent.getContext();
                int position = holder.getAbsoluteAdapterPosition();
                ToDoList toDoList = mToDoList.get(position);
                Intent intent = new Intent(context, TopAndDone.class);
                intent.putExtra("content",toDoList);
                context.startActivity(intent);
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListAdapter.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ToDoList toDoList = mToDoList.get(position);
        holder.thingImage.setImageResource(toDoList.getImageID());
        holder.thing.setText(toDoList.getThing());
        holder.deadline.setText(toDoList.getDeadline());
    }

    @Override
    public int getItemCount() {
        return mToDoList.size();
    }

}
