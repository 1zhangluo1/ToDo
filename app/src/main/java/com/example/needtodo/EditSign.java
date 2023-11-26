package com.example.needtodo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

public class EditSign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        EditText editSign = (EditText) findViewById(R.id.edit_sign);
        Button button = (Button) findViewById(R.id.sign_yes);
        button.setOnClickListener(v -> {
            User sign = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
            if(editSign.getText().toString().isEmpty()){
                Toast.makeText(EditSign.this, "签名内容不能为空", Toast.LENGTH_SHORT).show();
            }
            else if(sign.getSign() == null){
                sign.setSign(editSign.getText().toString());
                sign.save();
                EventBus.getDefault().post(new MessageSign("签名添加成功"));
                finish();
            }
            else if(sign.getSign() != null){
                sign.setSign(editSign.getText().toString());
                sign.updateAll("online = ?",String.valueOf(1));
                EventBus.getDefault().post(new MessageSign("签名更改成功"));
                finish();
            }
        });
    }
}