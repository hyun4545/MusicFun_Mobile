package com.example.myungjong.musicfun.Activity;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.myungjong.musicfun.Helper.BindHelper;
import com.example.myungjong.musicfun.Helper.MusicInfo;
import com.example.myungjong.musicfun.Helper.RegisterEvent;
import com.example.myungjong.musicfun.Model.Callback;
import com.example.myungjong.musicfun.Model.MusicHandlers;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.Service.MusicService;
import com.example.myungjong.musicfun.Service.UploadService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by myungjong on 2016/5/30.
 */
public class MyApplication extends Application  {



    private String music_title;
    private int music_position=-1;
    private List<MusicList> musicLists;
    private List<MusicList> original_musicLists;
    private MusicHandlers handlers;
    private int current_index=-1;
    public static final int SINGLE_CYCLE=1;
    public static final int ALL_CYCLE=2;
    public static final int ALL_LINE=3;
    public static final int RANDOM_CYCLE=4;
    public static final int MUSIC_LOADING=0;
    public  static  final  int MUSIC_LOADED=1;
    public  static  final  int MUSIC_SEEKCOMPLELED=2;
    private boolean isSeeking=false;
    private int playMode=ALL_CYCLE;
    private SharedPreferences sharedPreferences;
    private int music_count;
    int last_position;
    boolean isSetProgress;
    private MusicService musicService;
    private UploadService uploadService;
    private View lastView;
    private int lastMusicID;
    private int cursor_id;
    int music_id;
    //int position;
    Handler handler;
    public static String uri=" http://webapplication5020170423112144.azurewebsites.net/api/Account/";
    MusicHandlers handlers2;
    public Drawable windoww;
    private BindHelper<MusicService,MusicService.ServiceBinder> playerbindHelper;
    private BindHelper<UploadService,UploadService.ServiceBinder>uploadbindHelper;
    public RegisterEvent registerEvent=new RegisterEvent();

    public int getPlayMode(){
        return playMode;
    }
    /*public void setMusicLists(List<MusicList> lists){
        musicLists=lists;
        music_count=musicLists.size();
    }


    public boolean isListExit(){
        if(music_position!=-1){
            return musicLists.get(music_position)!=null;
        }else {
            return false;
        }
    }*/
    public void setOriginal_musicLists(List<MusicList> original_musicLists) {
        this.original_musicLists = original_musicLists;
    }

    public List<MusicList> getOriginal_musicLists(){
        return original_musicLists;
    }
    public MyApplication(){

    }



    public void setIsSeeking(boolean isSeeking){
        this.isSeeking=isSeeking;
    }
    public boolean getIsSeeking(){
        return isSeeking;
    }
    public MusicHandlers getHandlers() {
        return handlers;
    }
    public MusicHandlers getHandlers2() {
        return handlers2;
    }


   /* public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(Integer music_id) {

            this.music_id = music_id;

    }


    public int getMusic_duration() {
        Log.e("song time",musicLists.get(music_position).getSong_time());
        return Integer.parseInt(musicLists.get(music_position).getSong_time());
    }


    public void setPosition(int position){
        if(position<music_count){
            Log.e("positionn",Integer.toString(position));
            this.music_position=position;
            music_id=musicLists.get(position).getId();
        }
    }
    public int  getPosition(){
        return music_position;
    }
    public int getMusic_count(){
        return  music_count;
    }*/
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sharedPreferences=getSharedPreferences("token", Context.MODE_PRIVATE);
        handlers=new MusicHandlers();
        handlers2=new MusicHandlers();

        //playerbindHelper=new BindHelper<MusicService,MusicService.ServiceBinder>(this,MusicService.class,musicServiceCallback);
        //uploadbindHelper=new BindHelper<UploadService,UploadService.ServiceBinder>(this,UploadService.class,uploadServiceCallback);
        Log.e("playerbindHelper",Boolean.toString(playerbindHelper==null));
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public UploadService getUploadService() {
        return uploadService;
    }

    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    public  boolean musicIsBound(){
        return playerbindHelper.isBound();
    }
    public  boolean uploadIsBound(){
        return uploadbindHelper.isBound();
    }
   /* public  void bindMusicService(){
        playerbindHelper.bindService();
        //musicService=playerbindHelper.getService();
        Log.e("service",Boolean.toString(musicService==null));
        Log.e("rr",Boolean.toString(playerbindHelper.isRr()));
    }
    public  void bindUploadService(){
        uploadbindHelper.bindService();
        uploadService=uploadbindHelper.getService();

    }
    public  MusicService getMusicService(){
        if(!musicIsBound()){

            bindMusicService();
        }
        return  musicService;
    }
    public  UploadService getUploadService(){
        if(!uploadIsBound()){
            bindUploadService();
        }
        return  uploadService;
    }*/
   /* public String getTitle(){
        if(musicLists!=null&&music_position>-1){
            music_title=musicLists.get(music_position).getSong_title();
            return music_title;
        }else {
            return  null;
        }
    }

    public List<MusicList> getMusicLists() {
        return musicLists;
    }
*/

    //取得api音樂的id

    public void setToken(String token){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("token",token).commit();
    }
    public void deleteToken(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear().commit();
    }
    public String getToken(){
        return sharedPreferences.getString("token","");
    }


    public void setPlayMode(int mode){
        playMode=mode;
    }



 /*   public void setHandler(Handler handler) {
        this.handler = handler;
    }*/

    public void setSetProgress(boolean setProgress) {
        isSetProgress = setProgress;
    }






};

