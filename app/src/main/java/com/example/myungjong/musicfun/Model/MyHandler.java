package com.example.myungjong.musicfun.Model;

import android.os.Handler;

/**
 * Created by myungjong on 2016/9/17.
 */
public class MyHandler {
    private boolean isHandler=false;
    public Handler handler;
    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isHandler() {
        return isHandler;
    }

    public void setIsHandler(boolean isHandler) {
        this.isHandler = isHandler;
    }
}
