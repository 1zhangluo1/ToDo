package com.example.needtodo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.Calendar;

public class AddThingList extends BaseActivity {
    private EditText thingEditText;
    private EditText headlineEditText;
    private EditText deadlineEditText;
    private TextView enterDate;
    private int year,month,day;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thing_list);
        getDate();
        Intent intent = getIntent();
        String thisAccount = intent.getStringExtra("this_account");
        EditText fillDeadline = (EditText) findViewById(R.id.fill_deadline);
        thingEditText = (EditText) findViewById(R.id.add_thing);
        headlineEditText = (EditText) findViewById(R.id.add_headline);
        Button button = (Button) findViewById(R.id.add_yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thing = thingEditText.getText().toString();
                String headline = headlineEditText.getText().toString();
                String deadline = fillDeadline.getText().toString();
                if(thing.isEmpty()==false&&headline.isEmpty()==false&&deadline.isEmpty()==false){
                    User user = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
                    ThingsList add = new ThingsList();
                    add.setThings(thing);
                    add.setHeadline(headline);
                    add.setDeadline(deadline);
                    add.setToDefault("isOutDate");
                    add.setToDefault("isDone");
                    add.setToDefault("setTop");
                    user.getThingsListList().add(add);
                    add.save();
                    user.save();
                    Toast.makeText(AddThingList.this, "添加成功", Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(AddThingList.this, MainActivity.class);
                    startActivity(intent);
            }
             else if(thing.isEmpty()==true || headline.isEmpty()==true) {
                    Toast.makeText(AddThingList.this, "事件和标题不能为空", Toast.LENGTH_SHORT).show();
             }
             else if (deadline.isEmpty()==true){
                    Toast.makeText(AddThingList.this, "无截止时间", Toast.LENGTH_SHORT).show();
                }
            }
        });

        enterDate = findViewById(R.id.choose_deadline);
        enterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        fillDeadline.setText(year+"-"+(++month)+"-"+day);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(AddThingList.this, 0,listener,year,month,day);
                dialog.show();
            }
        });
    }
    private void getDate() {
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        Log.i("wxy","year"+year);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
    }
}

