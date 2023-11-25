package com.example.needtodo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class ChangeName extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        EditText editText = (EditText) findViewById(R.id.newName);
        Button button = (Button) findViewById(R.id.change_name_yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editText.getText().toString();
                User changeName = new User();
                if (newName.isEmpty()) {
                    Toast.makeText(ChangeName.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    changeName.setName(newName);
                    changeName.updateAll("online = ?", String.valueOf(1));
                    EventBus.getDefault().post(new MessageEvent("昵称修改成功"));
                    finish();
                }
            }
        });
    }
}