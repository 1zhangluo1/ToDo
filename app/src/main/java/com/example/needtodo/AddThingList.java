package com.example.needtodo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddThingList extends BaseActivity {
    private EditText thingEditText;
    private EditText headlineEditText;
    private EditText fillDeadline;
    private TextView enterDate;
    private int year,month,day;
    private Calendar calendar;
    private ImageButton back;
    private RadioGroup radioGroup;
    private int checkId = 0;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thing_list);
        radioGroup = findViewById(R.id.first_choose_type);
        back = findViewById(R.id.back_add);
        back.setOnClickListener(v -> finish());
        getDate();
        fillDeadline = findViewById(R.id.fill_deadline);
        thingEditText = findViewById(R.id.add_thing);
        headlineEditText = findViewById(R.id.add_headline);
        Button button = findViewById(R.id.add_yes);
        button.setOnClickListener(v -> {
            String thing = thingEditText.getText().toString();
            String headline = headlineEditText.getText().toString();
            String deadline = fillDeadline.getText().toString();
            String type = null;
            checkId = radioGroup.getCheckedRadioButtonId();
            if(!thing.isEmpty()&&!headline.isEmpty()&&!deadline.isEmpty()&&checkId != -1){
                radioButton = findViewById(checkId);
                type = radioButton.getText().toString();
                User user = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
                ThingsList add = new ThingsList();
                add.setThings(thing);
                add.setHeadline(headline);
                add.setDeadline(deadline);
                add.setToDefault("isOutDate");
                add.setToDefault("isDone");
                add.setToDefault("setTop");
                add.setType(type);
                user.getThingsListList().add(add);
                add.save();
                user.save();
                Toast.makeText(AddThingList.this, "添加成功", Toast.LENGTH_SHORT).show();
                ActivityCollector.finishAll();
                Intent intent1 = new Intent(AddThingList.this, MainActivity.class);
                startActivity(intent1);
        }
         else if(thing.isEmpty() || headline.isEmpty()) {
                Toast.makeText(AddThingList.this, "事件和标题不能为空", Toast.LENGTH_SHORT).show();
         }
         else if (deadline.isEmpty()){
                Toast.makeText(AddThingList.this, "无截止时间", Toast.LENGTH_SHORT).show();
            }
         else if (checkId == -1) {
                Toast.makeText(this, "还未选择类别", Toast.LENGTH_SHORT).show();
            }
        });

        enterDate = findViewById(R.id.choose_deadline);
        enterDate.setOnClickListener(v -> {
            DatePickerDialog.OnDateSetListener listener= (arg0, year, month, day) -> {
                Date date = new Date(year-1900,month,day);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fillDeadline.setText(simpleDateFormat.format(date));
            };
            DatePickerDialog dialog=new DatePickerDialog(AddThingList.this, 0,listener,year,month,day);
            dialog.show();
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

