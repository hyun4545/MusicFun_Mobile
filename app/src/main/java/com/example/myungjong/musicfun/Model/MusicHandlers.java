package com.example.myungjong.musicfun.Model;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

/**
 * Created by myungjong on 2016/9/28.
 */
public class MusicHandlers {
    ArrayList<Handler> handlers;

    public MusicHandlers(){
        handlers=new  ArrayList<Handler>();
    }

    public void addHandlers(Handler handler){
        if(!handlers.contains(handler)){

            handlers.add(handler);
        }
    }

    public ArrayList<Handler> getHandlers() {
        return handlers;
    }
}
