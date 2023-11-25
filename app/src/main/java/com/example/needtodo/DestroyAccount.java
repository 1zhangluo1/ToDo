package com.example.needtodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

public class DestroyAccount extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destroy_account);
        TextView yes = findViewById(R.id.destroy_yes);
        TextView no = findViewById(R.id.destroy_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.destroy_yes){
            User delete = LitePal.select("id").where("online = ?",String.valueOf(1)).findFirst(User.class);
            LitePal.delete(User.class,delete.getId());
            ActivityCollector.finishAll();
            Intent intent1 = new Intent(DestroyAccount.this, Login.class);
            startActivity(intent1);
            Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
        }
        else if (view.getId()==R.id.destroy_no){
            finish();
        }
    }
}