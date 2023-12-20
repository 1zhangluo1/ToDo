package com.example.needtodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftView,rightView;
        TextView leftText,rightText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftView = itemView.findViewById(R.id.left_chat);
            rightView = itemView.findViewById(R.id.right_chat);
            leftText = itemView.findViewById(R.id.left_answer);
            rightText = itemView.findViewById(R.id.right_question);
        }
    }

    List<Messages> messagesList;

    public MessageAdapter(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(chatView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Messages messages = messagesList.get(position);
        if (messages.getSendBy().equals(Messages.SEND_BY_ME)){
            holder.leftView.setVisibility(View.GONE);
            holder.rightView.setVisibility(View.VISIBLE);
            holder.rightText.setText(messages.getMessage());
        } else {
            holder.leftView.setVisibility(View.VISIBLE);
            holder.rightView.setVisibility(View.GONE);
            holder.leftText.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

}
