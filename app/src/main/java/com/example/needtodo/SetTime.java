package com.example.needtodo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SetTime extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        TextView textView = (TextView) findViewById(R.id.long_touch);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try{
                    Toast.makeText(SetTime.this, "成功长按事件", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "背景设置失败！",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }
}