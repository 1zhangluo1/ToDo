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

public class OutDateAdapter extends RecyclerView.Adapter<OutDateAdapter.ViewHolder> {

    private List<ToDoList> outList;
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
    public OutDateAdapter(List<ToDoList> outListList){
        outList = outListList;
    }
    @NonNull
    @Override
    public OutDateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.todolistView.setOnClickListener(v -> {
            Context context = parent.getContext();
            int position = holder.getAbsoluteAdapterPosition();
            ToDoList outDateList = outList.get(position);
            Intent intent = new Intent(context, QueryAndChange.class);
            intent.putExtra("content",outDateList);
            context.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OutDateAdapter.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ToDoList doneList = outList.get(position);
        holder.thingImage.setImageResource(doneList.getImageID());
        holder.thing.setText(doneList.getThing());
        holder.deadline.setText(doneList.getDeadline());
    }

    @Override
    public int getItemCount() {
        if(outList==null)return 0;
        return outList.size();
    }

}
