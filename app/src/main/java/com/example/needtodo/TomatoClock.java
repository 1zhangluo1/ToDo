package com.example.needtodo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.Random;

public class TomatoClock extends AppCompatActivity {

    private RelativeLayout background;
    private boolean isStop = false;
    private int[] imageId = new int[10];
    private ImageView back;
    private TextView focus_thing;
    private TextView countdown;
    private ImageView stop;
    private ImageView resume;
    private ImageView skip;
    private MyCountDownTimer myCountDownTimer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_clock);
        stop = findViewById(R.id.stopOrResume);
        stop.setImageResource(R.drawable.ic_stop);
        EventBus.getDefault().register(this);
        String id = this.getIntent().getStringExtra("thingsToSetClock");
        back();
        initBackGround(id);
        start();
    }

    private void initBackGround(String id) {
        imageId[0] = R.drawable.tomato1;
        imageId[1] = R.drawable.tomato2;
        imageId[2] = R.drawable.tomato3;
        imageId[3] = R.drawable.tomato4;
        imageId[4] = R.drawable.tomato5;
        imageId[5] = R.drawable.tomato6;
        imageId[6] = R.drawable.tomato7;
        imageId[7] = R.drawable.tomato8;
        imageId[8] = R.drawable.tomato9;
        imageId[9] = R.drawable.tomato10;
        setBack(imageId);
        focus_thing = findViewById(R.id.focus_thing);
        ThingsList background = LitePal.where("id = ?",id).findFirst(ThingsList.class);
        focus_thing.setText(background.getHeadline());
    }

    private void setBack(int[] imageId) {
        background = findViewById(R.id.tomato_mainRelative);
        background.setBackgroundResource(getRandomImage(imageId));
    }

    private int getRandomImage(int[] imageId) {
        Random random = new Random();
        int randomIndex = random.nextInt(imageId.length);
        int randomImage = imageId[randomIndex];
        return randomImage;
    }

    private void back() {
        back = findViewById(R.id.tomato_back);
        back.setOnClickListener(v -> {
            finish();
        });
    }

    private void reverseTimer(int Seconds) {
        countdown = findViewById(R.id.countdown);
        countdown.setOnClickListener(null);

        myCountDownTimer = new MyCountDownTimer(Seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (!isStop) {
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    countdown.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                } else if (isStop == true){}
            }
            public void onFinish() {
                Toast.makeText(TomatoClock.this, "时间到啦！！！", Toast.LENGTH_SHORT).show();
                runHintVoice();
                countdown.setText("点击结束");
                stop.setOnClickListener(null);
                skip.setOnClickListener(null);
                countdown.setOnClickListener(v -> {
                    finish();
                });
            }
        };
        myCountDownTimer.start();
        stopAndResume();
        skip();
    }

    private void start() {
        countdown = findViewById(R.id.countdown);
        countdown.setOnClickListener(v -> {
            Intent intent = new Intent(this, SetTime.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BackTime backTime) {
        reverseTimer(backTime.time);
    }

    private void stopAndResume() {
        stop = findViewById(R.id.stopOrResume);
        stop.setOnClickListener(v -> {
            if (isStop) {
                stop.setImageResource(R.drawable.ic_stop);
                myCountDownTimer.resume();
                isStop = false;
            } else if (isStop == false) {
                stop.setImageResource(R.drawable.ic_continue);
                myCountDownTimer.pause();
                isStop = true;
            }
        });
    }

    private void skip () {
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(v -> {
            myCountDownTimer.pause();
            runHintVoice();
            stop.setOnClickListener(null);
            skip.setOnClickListener(null);
            countdown.setText("点击结束");
            countdown.setOnClickListener(v1 -> {
                finish();
            });
        });
    }
    private void runHintVoice() {
        mediaPlayer = MediaPlayer.create(this,R.raw.finish);
        mediaPlayer.start();
    }
}