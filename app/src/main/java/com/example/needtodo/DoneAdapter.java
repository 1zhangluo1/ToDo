package com.example.needtodo;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.ViewHolder> {

    private List<ToDoList> mdoneList;
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
    public DoneAdapter(List<ToDoList> doneListList){
        mdoneList = doneListList;
    }
    @NonNull
    @Override
    public DoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.todolistView.setOnClickListener(v -> {
            Context context = parent.getContext();
            int position = holder.getAbsoluteAdapterPosition();
            ToDoList doneList = mdoneList.get(position);
            Intent intent = new Intent(context, QueryAndChange.class);
            intent.putExtra("content",doneList);
            context.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoneAdapter.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ToDoList doneList = mdoneList.get(position);
        holder.thingImage.setImageResource(doneList.getImageID());
        holder.thing.setText(doneList.getThing());
        holder.deadline.setText(doneList.getDeadline());
    }

    @Override
    public int getItemCount() {
        if(mdoneList==null)return 0;
        return mdoneList.size();
    }

}
