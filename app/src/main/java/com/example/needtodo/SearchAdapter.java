package com.example.needtodo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<com.example.needtodo.SearchAdapter.ViewHolder> {
    private List<ToDoList> searchResults;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView thing;
        TextView deadline;
        View searchView;

        public ViewHolder(View view) {
            super(view);
            searchView = view;
            thing = view.findViewById(R.id.todolist_name);
            deadline = view.findViewById(R.id.todolist_deadline);
        }
    }

    public SearchAdapter(List<ToDoList> searchResults) {
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public com.example.needtodo.SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.searchView.setOnClickListener(v -> {
            Context context = parent.getContext();
            int position = holder.getAbsoluteAdapterPosition();
            ToDoList searchList = searchResults.get(position);
            Intent intent = new Intent(context, QueryAndChange.class);
            intent.putExtra("content", searchList);
            context.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.needtodo.SearchAdapter.ViewHolder holder, int position) {
        ViewHolder viewHolder = holder;
        ToDoList searchList = searchResults.get(position);
        holder.thing.setText(searchList.getThing());
        holder.deadline.setText(searchList.getDeadline());
    }

    @Override
    public int getItemCount() {
        if (searchResults == null) return 0;
        return searchResults.size();
    }
}
