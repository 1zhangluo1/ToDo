package com.example.needtodo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class Register extends AppCompatActivity {
    private EditText editText_account;
    private EditText editText_password;
    private EditText editText_testpassword;
    private EditText editText_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText_account = (EditText) findViewById(R.id.set_account);

        editText_password = (EditText) findViewById(R.id.set_password);

        editText_testpassword = (EditText) findViewById(R.id.test_password);

        editText_userName = (EditText) findViewById(R.id.set_name);

        Button doregister = (Button) findViewById(R.id.do_register);

        doregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_userName.getText().toString();
                String testPassword = editText_testpassword.getText().toString();
                String setAccount = editText_account.getText().toString();
                String setPassword = editText_password.getText().toString();
                Connector.getDatabase();
                User id = new User();
                id.setAccount(setAccount);
                id.setPassword(setPassword);
                id.setName(name);
                List<User> accounts = LitePal.select("account").find(User.class);
                boolean judge = false;
                for (User account:accounts){
                    if(account.getAccount().equals(setAccount)){judge = true;
                    break;}
                }
                if(testPassword.isEmpty()==true||setAccount.isEmpty()==true||setPassword.isEmpty()==true) {
                    Toast.makeText(Register.this, "账号或密码为空，请重新输入", Toast.LENGTH_SHORT).show();
                }
                else if (name.isEmpty()==true){
                    Toast.makeText(Register.this, "用户昵称为空", Toast.LENGTH_SHORT).show();
                }
                else if(setPassword.compareTo(testPassword)!=0){
                    Toast.makeText(Register.this, "两次输入密码不一致，请检查后重新输入", Toast.LENGTH_SHORT).show();
                }
                else if(judge==true){
                    Toast.makeText(Register.this, "该账号已创建，无需重复创建", Toast.LENGTH_SHORT).show();
                }
                else if(id.save()){
                    Toast.makeText(Register.this, "账户创建成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}