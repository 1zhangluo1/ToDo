package com.example.needtodo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected void setTransparentStatusBar(boolean isDark) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (isDark)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//全屏布局|深色状态(黑色字体)
        else
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);//设置透明色
    }


    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("date",simpleDateFormat.format(date));
        User this_account = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        long this_id = this_account.getId();
        List<ThingsList> outDateThings = LitePal.where("deadline < ? and isDone = ? and user_id = ? ", simpleDateFormat.format(date), String.valueOf(0), String.valueOf(this_id)).find(ThingsList.class);
        Log.d("tag",outDateThings.toString());
        if (outDateThings != null) {
            for (ThingsList setOut : outDateThings) {
                setOut.setOutDate(true);
                setOut.update(setOut.getId());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
