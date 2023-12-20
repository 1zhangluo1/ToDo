package com.example.needtodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User isSkip = LitePal.select("online").where("online = ?", String.valueOf(1)).findFirst(User.class);
                if (isSkip != null) skipLogin();
                else login();
            }
        }, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void login() {
        Intent intent = new Intent(WelcomeActivity.this, Login.class);
        startActivity(intent);
        WelcomeActivity.this.fileList();
        finish();
    }

    private void skipLogin() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        WelcomeActivity.this.fileList();
        finish();
    }

}