package com.example.myungjong.musicfun.Helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by myungjong on 2017/1/17.
 */

public class MusicFun {
   public interface MusicFunListener{
        void onProgressUpdate(int progress);
        void onSeekComplete();
       void onComplete();
        void onPrepare();
        void onPrepared();
       void onBuffering();
        void onPause();
        void onStart();
    }

    ArrayList<MusicFunListener> musicFunListeners=new ArrayList<>();
    final Map<String,String> headers=new HashMap<String, String>();
    String URL="http://webapplication5020170423112144.azurewebsites.net/api/Account/GetStream/";
    private String music_title;
    private int position=-1;
    private int precent=0;



    private List<MusicList> musicLists;
    private boolean isSeeking;
    private boolean isPreprare;
    private int playMode;
    Thread musicThread,progressThread;
    String token;
    Context context;
    MyApplication application;
    private  int music_count;
    NotificationManager notificationManager;
    RemoteViews remoteViews;
    Notification notification;




    private MediaPlayer mediaPlayer;
    private ArrayList<Integer> songsRecord;

    public MusicFun(String token,Context context){

        this.token=token;
        this.context=context;
        application= (MyApplication) context.getApplicationContext();
        playMode=MyApplication.ALL_CYCLE;

       // music_count=musicListCount();

        songsRecord=new ArrayList<Integer>();

       // InitMusicPlayer();
       // InitMusicPlayer();
    }

    public String getMusic_title() {
        return musicLists.get(position).getSong_title();
    }
    private int musicListCount(){

        if(getMusicInfo().getMusicLists()!=null){
            return getMusicInfo().getMusicLists().size();
        }else {
            return 0;
        }
    }
    public int getProgress(){return mediaPlayer.getCurrentPosition();}
    public void setSeeking(boolean seeking) {
        isSeeking = seeking;
    }
    public void setSelect(boolean select){musicLists.get(position).setSelect(select);}
    public boolean isSelect(){return musicLists.get(position).isSelect();}
    public void setPlay(boolean play){musicLists.get(position).setPlay(play);}
    public boolean getPlay(){return musicLists.get(position).isPlay();}
    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }


    public boolean isPreprare() {
        return isPreprare;
    }

    private void setPreprare(boolean preprare) {
        isPreprare = preprare;
    }


    public void start(){

        sendOnStart();
        mediaPlayer.start();
        progressUpdate();
    }
    public void pause(){
        sendOnPause();
        mediaPlayer.pause();
    }
    public void seekTo(int ms){
        mediaPlayer.seekTo(ms);

    }
    private void InitMusicPlayer(){
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            //boolean isBuffering=false;
            //int count=0;
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
              precent= percent;

                Log.e("percent22",Integer.toString(percent));
                /*if(getCurrentPrecent()==percent){
                   count++;
                    Log.e("bufferingcount",Integer.toString(count));
                    if(count>2){
                        Log.e("buffering","true");
                        isBuffering=true;
                        sendOnPrepare();
                    }
                }else {
                    if(isBuffering){
                        Log.e("buffering","false");
                        sendOnPrepared();
                        isBuffering=false;
                    }
                }*/
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("mediaPlayer","Completion");
                if(!isSeeking){
                    switch (playMode){

                        case MyApplication.ALL_CYCLE:

                        case MyApplication.ALL_LINE:

                        case MyApplication.RANDOM_CYCLE:
                          nextCircle();
                          sendOnComplete();

                            break;

                        case MyApplication.SINGLE_CYCLE:
                            mediaPlayer.start();
                            break;
                    }

                }else {
                    mediaPlayer.start();
                    sendOnSeekComplete();
                    isSeeking=false;
                }


            }
        });


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e("mediaPlayer","Prepared");

                mediaPlayer.start();
                //mediaPlayer.notify();
                sendOnPrepared();
                progressUpdate();

            }
        });

    }


    //播放下一首
    public void nextCircle(){
        Log.e("playMode",Integer.toString(playMode) );
        Log.e("music_count",Integer.toString(musicListCount()) );
        switch (playMode){
            case MyApplication.ALL_CYCLE:
                if(getMusicInfo().getPosition()+1<musicListCount()){
                    getMusicInfo().setPosition(getMusicInfo().getPosition()+1);
                    Log.e("positionn2",Integer.toString(getMusicInfo().getPosition()));
                }else {
                    getMusicInfo().setPosition(0);
                }
                break;
            case MyApplication.RANDOM_CYCLE:
                    Random rand = new Random();
                    int  n = rand.nextInt(music_count-1);
                getMusicInfo().setPosition(n);

                break;
            case MyApplication.SINGLE_CYCLE:
                getMusicInfo().setPosition(position);
                break;
        }
         playMusic();


    }
    //播放前一首
    public void prevCircle(){
        switch (playMode){
            case MyApplication.ALL_CYCLE:
                if(music_count>1){
                    if(getMusicInfo().getPosition()>0){
                        getMusicInfo().setPosition(getMusicInfo().getPosition()-1);

                    }else {
                        getMusicInfo().setPosition(musicListCount()-1);
                    }
                }
                break;
            case MyApplication.RANDOM_CYCLE:
                getMusicInfo().setId(songsRecord.get(songsRecord.size()-2));
                break;

            case MyApplication.SINGLE_CYCLE:
                getMusicInfo().setPosition(getMusicInfo().getPosition());
               break;
        }
        playMusic();
    }

    public void addMusicFunListener(MusicFunListener musicFunListener){
        musicFunListeners.add(musicFunListener);
    }

    private Handler sendOnProgressUpdate() {
        return new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                for (MusicFunListener musicFunListener:musicFunListeners){
                    Log.e("ProgressUpdate",Integer.toString(msg.arg1));
                    musicFunListener.onProgressUpdate(msg.arg1);
                }
            }
        };

           }

    private void sendOnSeekComplete() {
               for (MusicFunListener musicFunListener:musicFunListeners){
                   musicFunListener.onSeekComplete();
               }
           }
    private void sendOnBuffering() {
        for (MusicFunListener musicFunListener:musicFunListeners){
            musicFunListener.onBuffering();
        }
    }

    private void sendOnComplete() {
        for (MusicFunListener musicFunListener:musicFunListeners){
            musicFunListener.onComplete();
        }
    }

    private void sendOnPrepare() {
        setPreprare(true);

                for (MusicFunListener musicFunListener : musicFunListeners) {
                    musicFunListener.onPrepare();
                    //Log.e("sendOnPrepare", "sendOnPrepare");
                }

    }

    private void sendOnPrepared() {
        setPreprare(false);

                for (MusicFunListener musicFunListener:musicFunListeners){
                    musicFunListener.onPrepared();
                }

           }

    private void sendOnPause() {
        for (MusicFunListener musicFunListener:musicFunListeners){
            musicFunListener.onPause();
        }
    }

    private void sendOnStart() {
        for (MusicFunListener musicFunListener:musicFunListeners){
            musicFunListener.onStart();
        }
    }



    public void playMusic(){
        if(!isPlayerExisted()){
            mediaPlayer=new MediaPlayer();
        }
        sendOnPrepare();
        if(progressThread!=null){
            progressThread.interrupt();
        }
        songsRecord.add(getMusicInfo().getId());
        if(musicThread!=null){
            musicThread=null;
        }

        musicThread=new Thread(){
            @Override
            public void run() {

                try {
                    Uri uri= Uri.parse(URL+getMusicInfo().getId());
                    headers.put("access_token",token);
                    Log.e("pre_reset",token);
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        Log.e("mediaPlayer null","false");
                    }

                    mediaPlayer =new MediaPlayer();
                    Log.e("idle","idle");
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    InitMusicPlayer();
                    mediaPlayer.setDataSource(context.getApplicationContext(),uri,headers);
                    mediaPlayer.prepare();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                super.run();
            }
        };
        musicThread.start();

    }
    private MusicInfo getMusicInfo(){
        return MusicInfo.getInstance();
    }
    private int getCurrentPrecent(){
        int i=(mediaPlayer.getCurrentPosition()*100)/getMusicInfo().getDuration();
        return i;
    }
    public boolean isPlay(){
        if(isPlayerExisted()){
            return mediaPlayer.isPlaying();
        }else {
            return  false;
        }

    }
    public boolean isPlayerExisted(){
        return mediaPlayer!=null;
    }

   private void progressUpdate(){

       if(progressThread!=null){
           progressThread=null;
       }
       progressThread=new Thread(){
           int lastPosition=-1;
           int count=0;
           boolean isPrepare=false;
           Handler handler=new Handler();
           @Override
           public void run() {
               super.run();
               int position;
               while(isPlay()&&isPlayerExisted()){
                   try {
                       Thread.sleep(200);
                   } catch (InterruptedException e) {}

                       position = mediaPlayer.getCurrentPosition();

                   Log.e("getCurrentPosition",Integer.toString(position));
                   if(lastPosition==position){
                       count++;
                       if(count>3){
                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                               @Override
                               public void run() {

                                   Log.e("isPreprare2","true");
                                   isPreprare=true;
                                   sendOnBuffering();

                               }
                           });
                       }
                   }else {
                       if(isPreprare()){
                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                               @Override
                               public void run() {

                                   Log.e("isPreprare2","false");
                                   isPreprare=false;
                                   sendOnPrepared();

                               }
                           });
                       }
                   }

                   lastPosition=position;


                   Message message=Message.obtain();
                   message.arg1=position;
                   sendOnProgressUpdate().sendMessage(message);


               }

           }
       };
       progressThread.start();
    }

}
