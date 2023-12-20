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

import overwrite.InsideRecyclerView;

public class InsideAdapter extends InsideRecyclerView.Adapter<InsideAdapter.ViewHolder> {

    private List<Selection.SingleSelection> inside;
    private Context mContext;

    public InsideAdapter(Context context, List<Selection.SingleSelection> inside) {
        mContext = context;
        this.inside = inside;
    }

    @NonNull
    @Override
    public InsideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inside, parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(v -> {
            Context context = parent.getContext();
            int position = holder.getAbsoluteAdapterPosition();
            Selection.SingleSelection single_choose = inside.get(position);
            Intent intent = new Intent(context, QueryAndChange.class);
            intent.putExtra("selection", single_choose);
            context.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(inside.get(position).getHeadline());
    }

    @Override
    public int getItemCount() {
        return inside.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.inside_square);
            textView = itemView.findViewById(R.id.inside_headline);
        }
    }
}