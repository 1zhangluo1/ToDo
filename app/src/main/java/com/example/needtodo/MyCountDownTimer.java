package com.example.needtodo;

import android.os.CountDownTimer;

public class MyCountDownTimer extends CountDownTimer {

    private long mInitialTimeLeft;
    private long mTimeLeft;

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        mInitialTimeLeft = millisInFuture;
        mTimeLeft = millisInFuture;
    }
    @Override
    public void onTick(long millisUntilFinished) {
        mTimeLeft = millisUntilFinished;
    }
    @Override
    public void onFinish() {
    }
    public void pause() {
        this.cancel();
        mTimeLeft = this.mInitialTimeLeft - mTimeLeft;
    }
    public void resume() {
        this.cancel();
        this.mInitialTimeLeft = mTimeLeft;
        this.start();
    }

}
