package com.example.myungjong.musicfun.Helper;

import android.content.Intent;
import android.util.Log;

import com.example.myungjong.musicfun.Model.MusicList;

import java.util.List;

/**
 * Created by myungjong on 2017/2/27.
 */

public class MusicInfo {

    private String title;
    private String author;
    private int duration;
    private int position=-1;
    private List<MusicList> musicLists;
    private int id;
    private int cursor;
    private static MusicInfo instance;

    private MusicInfo(){

    }

    public void setMusicInfo( List<MusicList> musicLists,int position){
      this.position=position;
        this.musicLists=musicLists;
    }
    public static MusicInfo getInstance(){
        if(instance==null){
            instance=new MusicInfo();
        }
        return instance;
    }

    /*public void setMusicLists(List<MusicList> musicLists) {
        this.musicLists = musicLists;
    }

    public void setId(int id){
        this.id=id;
        cursor=0;
    }
    public void setPosition(int position) {
        this.position = position;
        title=musicLists.get(position).getSong_title();
        duration=Integer.parseInt(musicLists.get(position).getSong_time());
        author=musicLists.get(position).getAuthor();
        Log.e("id",Integer.toString(musicLists.get(position).getId()));
        id=musicLists.get(position).getId();

    }
*/
    public String getAuthor() {
        return musicLists.get(position).getAuthor();
    }

    public int getDuration() {
        return Integer.parseInt(musicLists.get(position).getSong_time());
    }
    public boolean isListExit(){
        if(position!=-1){
            return musicLists.get(position)!=null;
        }else {
            return false;
        }}
    public int getId() {
        if(position==-1){
            return -1;
        }else {
            return musicLists.get(position).getId();
        }


    }
    public void setId(int id){
        this.id=id;
    }
    public void setPosition(int position) {
        this.position = position;

    }
    public List<MusicList> getMusicLists() {
        return musicLists;
    }

    public int getPosition() {
        return position;
    }

    public String getTitle() {
        return musicLists.get(position).getSong_title();
    }
}
