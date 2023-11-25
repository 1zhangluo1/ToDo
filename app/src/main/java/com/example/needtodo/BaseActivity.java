package com.example.needtodo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate (@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        User this_account = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
        long this_id = this_account.getId();
        List<ThingsList> notification = LitePal.where("deadline = ? and isDone = ? and user_id = ?",simpleDateFormat.format(date),String.valueOf(0),String.valueOf(this_id)).find(ThingsList.class);
        int i=1,j=1;
        for (ThingsList deadlines : notification) {
                Notification(deadlines,i);
                i++;
        }
        List<ThingsList> soonDead = LitePal.where("isDone = ? and deadline > ? and user_id = ?",String.valueOf(0),simpleDateFormat.format(date),String.valueOf(this_id)).find(ThingsList.class);
        if (soonDead != null) {
            List<String> times = new ArrayList<>();
            for (ThingsList time : soonDead) {
                times.add(time.getDeadline());
            }
            if (!times.isEmpty()) {
                String minDate = Collections.min(times);
                List<ThingsList> soonNotice = LitePal.where("deadline = ?", minDate).find(ThingsList.class);
                for (ThingsList notifySoon : soonNotice) {
                    DeadSoon(notifySoon, i, j);
                    j++;
                    }
                }
            }
        List<ThingsList> outDateThings = LitePal.where("deadline < ?", simpleDateFormat.format(date)).find(ThingsList.class);
        for (ThingsList setOut : outDateThings) {
            setOut.setOutDate(true);
            setOut.updateAll("deadline < ?",simpleDateFormat.format(date));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void DeadSoon(ThingsList thingsList,int i,int j) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
        Notification notification1 = new NotificationCompat.Builder(BaseActivity.this, "default")
                .setContentTitle(thingsList.getHeadline()+" 即将截止")
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
        manager.notify(j+i, notification1);
    }
    protected void Notification(ThingsList thingsList,int i) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<ThingsList> notification = LitePal.findAll(ThingsList.class);
        for (ThingsList deadlines : notification) {
            if (simpleDateFormat.format(date).equals(deadlines.getDeadline())) {
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "default";
                    String channelName = "默认通知";
                    manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
                }
                Notification notification1 = new NotificationCompat.Builder(BaseActivity.this, "default")
                        .setContentTitle("有事项在今天截止!!!")
                        .setContentText(thingsList.getHeadline())
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_logincenter)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nav_logo1))
                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                        .setVibrate(new long[]{0, 1000, 1000, 1000})
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(true)
                        .build();
                manager.notify(i, notification1);
            }
        }
    }
}
