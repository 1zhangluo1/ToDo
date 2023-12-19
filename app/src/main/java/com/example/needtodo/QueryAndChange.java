package com.example.needtodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private ImageView tomatoTime;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String id;
    private int checkedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_and_change);
        radioGroup = findViewById(R.id.choose_type);
        title = findViewById(R.id.headline);
        if (this.getIntent().getParcelableExtra("content") != null) {
            ToDoList toDoList = this.getIntent().getParcelableExtra("content");
            id = String.valueOf(toDoList.getId());
        } if (this.getIntent().getParcelableExtra("selection") != null) {
            Selection.SingleSelection point_id = this.getIntent().getParcelableExtra("selection");
            id = String.valueOf(point_id.getId());
        }
        ThingsList showThing = LitePal.where("id = ?", id).findFirst(ThingsList.class);
        title.setText(showThing.getHeadline());
        text = findViewById(R.id.text);
        text.setText(showThing.getThings());
        deadline = findViewById(R.id.deadline);
        deadline.setText(Objects.toString(showThing.getDeadline()));
        ThingsList thisType = LitePal.where("id = ?",id).findFirst(ThingsList.class);
        String defaultType = thisType.getType().toString();
        if (defaultType.equals("学习")){
            radioButton = findViewById(R.id.study);
            radioGroup.check(radioButton.getId());
        } else if (defaultType.equals("工作")) {
            radioButton = findViewById(R.id.work);
            radioGroup.check(radioButton.getId());
        } else if (defaultType.equals("生活")) {
            radioButton = findViewById(R.id.life);
            radioGroup.check(radioButton.getId());
        } else if (defaultType.equals("家庭")) {
            radioButton = findViewById(R.id.family);
            radioGroup.check(radioButton.getId());
        } else if (defaultType.equals("其它")) {
            radioButton = findViewById(R.id.selection_false);
            radioGroup.check(radioButton.getId());
        }
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
        delete = findViewById(R.id.delete1);
        delete.setOnClickListener(v -> {
            LitePal.deleteAll(ThingsList.class, "id = ?", id);
            EventBus.getDefault().post(new UpdateList("删除成功"));
            Toast.makeText(QueryAndChange.this, "删除成功", Toast.LENGTH_SHORT).show();
            finish();
        });
        save = findViewById(R.id.save_new);
        save.setOnClickListener(v -> {
            checkedId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(checkedId);
            String type = radioButton.getText().toString();
            String headline = title.getText().toString();
            String content = text.getText().toString();
            ThingsList update = new ThingsList();
            update.setHeadline(headline);
            update.setThings(content);
            update.setType(type);
            update.updateAll("id = ?", String.valueOf(id));
            EventBus.getDefault().post(new UpdateList("修改成功"));
            Toast.makeText(QueryAndChange.this, "修改成功", Toast.LENGTH_SHORT).show();
            finish();
        });
        enterTomato(id);
    }

    private void enterTomato(String id) {
        tomatoTime = findViewById(R.id.tomatoClock);
        tomatoTime.setOnClickListener(v -> {
            Intent intent = new Intent(this, TomatoClock.class);
            intent.putExtra("thingsToSetClock", id);
            startActivity(intent);
        });
    }
}