package com.example.needtodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import overwrite.InsideRecyclerView;
import overwrite.OutRecyclerView;

public class OutAdapter extends OutRecyclerView.Adapter<OutAdapter.ViewHolder> {

    private List<Selection> selections ;
    private Context mContext;
    public OutAdapter(Context context, List<Selection> selections) {
        mContext = context;
        this.selections = selections;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_out, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.insideRecyclerView.getAdapter() == null) {
            holder.mTitle.setText(selections.get(position).getTitle());
            holder.insideRecyclerView.setAdapter(new InsideAdapter(mContext,selections.get(position).getSelections()));
        }
    }

    @Override
    public int getItemCount() {
        return selections.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private InsideRecyclerView insideRecyclerView;
        private TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_title);
            insideRecyclerView = itemView.findViewById(R.id.inside_recyclerview);
            insideRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }
    }
}