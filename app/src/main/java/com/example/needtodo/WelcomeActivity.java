package com.example.needtodo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sendNotification();
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

    private void sendNotification() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("TIMETIME",simpleDateFormat.format(date));
        User this_account = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        long this_id = this_account.getId();
        List<ThingsList> notification = LitePal.where("deadline = ? and user_id = ? and isDone = ?",simpleDateFormat.format(date),String.valueOf(this_id),String.valueOf(0)).find(ThingsList.class);
        int i = 1, j = 1;
        for (ThingsList onThisDay : notification) {
            DeadSoon(onThisDay, i);
            i++;
        }
        List<ThingsList> closeDead = LitePal.where("isDone = ? and deadline > ? and user_id = ?", String.valueOf(0), simpleDateFormat.format(date), String.valueOf(this_id)).find(ThingsList.class);

        if (closeDead != null) {
            List<String> times = new ArrayList<>();
            for (ThingsList time : closeDead) {
                times.add(time.getDeadline());
            }
            if (!times.isEmpty()) {
                String minDate = Collections.min(times);
                List<ThingsList> soonNotice = LitePal.where("deadline = ?", minDate).find(ThingsList.class);
                Log.d("tag",soonNotice.toString());
                for (ThingsList notifyClose : soonNotice) {
                    Notification(notifyClose, i, j);
                    j++;
                }
            }
        }
    }

    private void DeadSoon(ThingsList thingsList, int i) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
        Notification notification1 = new NotificationCompat.Builder(com.example.needtodo.WelcomeActivity.this, "default")
                .setContentTitle(thingsList.getHeadline() + " 即将截止")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(thingsList.getThings()))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logincenter)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nav_logo1))
                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        manager.notify(i, notification1);
    }
    private void Notification(ThingsList thingsList, int i, int j) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
        Notification notification1 = new NotificationCompat.Builder(com.example.needtodo.WelcomeActivity.this, "default")
                .setContentTitle(thingsList.getHeadline() + "的截止时间快到了")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(thingsList.getThings()))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logincenter)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nav_logo1))
                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        manager.notify(i+j, notification1);
    }
}