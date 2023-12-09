package com.example.needtodo;

import android.os.CountDownTimer;

public class MyCountDownTimer extends CountDownTimer {

    private long totalTime;
    private long leftTime;
    private boolean mIsPaused;

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        totalTime = millisInFuture;
        leftTime = millisInFuture;
        mIsPaused = false;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (!mIsPaused) {
            leftTime = millisUntilFinished;
        }
    }

    @Override
    public void onFinish() {
        // 倒计时结束时的操作
    }

    public void pause() {
        if (!mIsPaused) {
            this.cancel();
            leftTime = this.totalTime - leftTime;
            mIsPaused = true;
        }
    }

    public void resume() {
        if (mIsPaused) {
            this.totalTime = leftTime;
            mIsPaused = false;
            this.start();
        }
    }
}
