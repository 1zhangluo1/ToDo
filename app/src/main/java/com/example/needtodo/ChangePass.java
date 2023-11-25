package com.example.needtodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

public class ChangePass extends BaseActivity {

    private EditText preeditText;
    private EditText neweditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        preeditText = (EditText) findViewById(R.id.prepass);
        neweditText = (EditText) findViewById(R.id.newpass);
        User password = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
        Button button = (Button) findViewById(R.id.change_yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prePassword = preeditText.getText().toString();
                String newPassword = neweditText.getText().toString();
                if(password.getPassword().equals(prePassword) && newPassword.isEmpty() == false){
                    User update = new User();
                    update.setPassword(newPassword);
                    update.updateAll("account = ?",password.getAccount());
                    Toast.makeText(ChangePass.this, "修改成功", Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishAll();
                    Intent intent1 = new Intent(ChangePass.this, Login.class);
                    startActivity(intent1);
                }
                else if(password.getPassword().compareTo(prePassword) != 0){
                    Toast.makeText(ChangePass.this, "原密码不对，修改失败", Toast.LENGTH_SHORT).show();
                }
                else if (newPassword.isEmpty() == true) {
                    Toast.makeText(ChangePass.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}