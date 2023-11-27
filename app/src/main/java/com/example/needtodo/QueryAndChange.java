package com.example.needtodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.Objects;

public class QueryAndChange extends BaseActivity {

    private EditText title;
    private EditText text;
    private TextView deadline;
    private ImageView back;
    private ImageView save;
    private ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_and_change);
        title = findViewById(R.id.headline);
        ToDoList toDoList=this.getIntent().getParcelableExtra("content");
        title.setText(toDoList.getThing());
        String id = String.valueOf(toDoList.getId());
        Log.d("MY ID",id);
        ThingsList showThing = LitePal.where("id = ?",String.valueOf(toDoList.getId())).findFirst(ThingsList.class);
        text = findViewById(R.id.text);
        text.setText(showThing.getThings());
        Log.d("tag",showThing.toString());
        deadline = findViewById(R.id.deadline);
        deadline.setText(Objects.toString(showThing.getDeadline()));
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        delete = findViewById(R.id.delete1);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAll(ThingsList.class,"id = ?",String.valueOf(toDoList.getId()));
                EventBus.getDefault().post(new UpdateList("删除成功"));
                finish();
            }
        });
        save = findViewById(R.id.save_new);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headline = title.getText().toString();
                String content = text.getText().toString();
                ThingsList update = new ThingsList();
                update.setHeadline(headline);
                update.setThings(content);
                update.updateAll("id = ?",String.valueOf(toDoList.getId()));
                Toast.makeText(QueryAndChange.this, "修改成功", Toast.LENGTH_SHORT).show();
                ActivityCollector.finishAll();
                Intent intent = new Intent(QueryAndChange.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}