package com.example.needtodo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

public class SetTime extends AppCompatActivity {
    private EditText self;
    private Button yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        yes();
    }

    private void yes() {
        yes = findViewById(R.id.start_time);
        yes.setOnClickListener(v -> {
            self = findViewById(R.id.define_myself);
            if (self.getText().toString().isEmpty() == false) {
                int time = Integer.parseInt(self.getText().toString());
                if (time > 0 && time <= 300 ) {
                    time *= 60;
                    EventBus.getDefault().post(new BackTime(time));
                    finish();
                } else {
                    Toast.makeText(this, "输入的时间不在范围内，单次最多300分钟", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "还未设置时间", Toast.LENGTH_SHORT).show();
            }
        });
    }

}