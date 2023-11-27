package com.example.needtodo;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

public class TopAndDone extends AppCompatActivity {

    RelativeLayout setTop;
    RelativeLayout setDone;
    TextView topAndCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_and_done);
        ToDoList toDoList=this.getIntent().getParcelableExtra("content");
        long id = toDoList.getId();
        topOrCancel(id);
        setDone = findViewById(R.id.set_done);
        setDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThingsList isDone = LitePal.where("id = ?",String.valueOf(id)).findFirst(ThingsList.class);
                isDone.setDone(true);
                isDone.updateAll("id = ?",String.valueOf(id));
                EventBus.getDefault().post(new UpdateList("成功删除"));
                finish();
            }
        });
    }

    private void topOrCancel(long id) {
        ThingsList topOrCancel = LitePal.where("id = ?",String.valueOf(id)).findFirst(ThingsList.class);
        if(topOrCancel.isSetTop()==true){
            topAndCancel = findViewById(R.id.topOrCancel);
            topAndCancel.setText("取消置顶");
            setTop = findViewById(R.id.set_top);
            setTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topOrCancel.setToDefault("setTop");
                    topOrCancel.updateAll("id = ?",String.valueOf(id));
                    EventBus.getDefault().post(new UpdateList("已取消置顶"));
                    finish();
                }
            });
        }
        else if (topOrCancel.isSetTop()==false){
            setTop = findViewById(R.id.set_top);
            setTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThingsList isTop = LitePal.where("id = ?",String.valueOf(id)).findFirst(ThingsList.class);
                    isTop.setSetTop(true);
                    isTop.updateAll("id = ?",String.valueOf(id));
                    EventBus.getDefault().post(new UpdateList("已成功置顶"));
                    finish();
                }
            });
        }
    }
}