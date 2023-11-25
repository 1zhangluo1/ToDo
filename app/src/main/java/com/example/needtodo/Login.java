package com.example.needtodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText inputaccounteditText;
    private EditText inputpasswordeditText;
    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean isHidePass = false;
    private ImageView displayPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        inputaccounteditText = (EditText)findViewById(R.id.account);
        inputpasswordeditText = (EditText)findViewById(R.id.password);
        displayPass = findViewById(R.id.hideAndSeePass);
        displayPass.setOnClickListener(this);
        displayPass.setImageResource(R.drawable.ic_hidepass);
        rememberPass = findViewById(R.id.memorize_password);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            inputaccounteditText.setText(pref.getString("account",""));
            inputpasswordeditText.setText(pref.getString("password",""));
            rememberPass.setChecked(true);
        }
        Button register = (Button) findViewById(R.id.enter_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAccount = inputaccounteditText.getText().toString();
                String inputPassword = inputpasswordeditText.getText().toString();
                User user = LitePal.select("password").where("account = ?",inputAccount).findFirst(User.class);
                if (user!=null && user.getPassword().equals(inputPassword)) {
                         editor = pref.edit();
                         if(rememberPass.isChecked()){
                    editor.putString("account", inputAccount);
                    editor.putString("password", inputPassword);
                    editor.putBoolean("remember_password", true);} else{editor.clear();}
                         editor.apply();
                         User online = new User();
                         online.setOnline(true);
                         online.updateAll("account = ?",inputAccount);
                         User online1 = new User();
                         online1.setToDefault("online");
                         online1.updateAll("account <> ?",inputAccount);
                         Intent intent = new Intent(Login.this, MainActivity.class);
                         intent.putExtra("this_account",inputAccount);
                         startActivity(intent);
                         finish();
                }
                else {
                    Toast.makeText(Login.this, "登录失败", Toast.LENGTH_SHORT).show();
                };
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.hideAndSeePass) {
            if(isHidePass){
                displayPass.setImageResource(R.drawable.ic_seepass);
                HideReturnsTransformationMethod method = HideReturnsTransformationMethod.getInstance();
                inputpasswordeditText.setTransformationMethod(method);
                isHidePass=false;
            } else {
                displayPass.setImageResource(R.drawable.ic_hidepass);
                TransformationMethod method1 = PasswordTransformationMethod.getInstance();
                inputpasswordeditText.setTransformationMethod(method1);
                isHidePass = true;
            }
            int index = inputpasswordeditText.getText().toString().length();
            inputpasswordeditText.setSelection(index);
        }
    }
}