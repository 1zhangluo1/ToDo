package com.example.needtodo;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

public class TopAndDone extends AppCompatActivity {

    RelativeLayout setTop;
    RelativeLayout setDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_and_done);
        ToDoList toDoList=this.getIntent().getParcelableExtra("content");
        long id = toDoList.getId();
        setTop = findViewById(R.id.set_top);
        setDone = findViewById(R.id.set_done);
        setDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThingsList isDone = LitePal.where("id = ?",String.valueOf(id)).findFirst(ThingsList.class);
                isDone.setDone(true);
                isDone.updateAll("id = ?",String.valueOf(id));
                Toast.makeText(TopAndDone.this, "已添加至完成列表", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}