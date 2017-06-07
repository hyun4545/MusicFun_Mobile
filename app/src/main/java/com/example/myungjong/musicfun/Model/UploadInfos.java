package com.example.myungjong.musicfun.Model;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by myungjong on 2016/8/25.
 */
public class UploadInfos {
    ArrayList<String> music_titles;
    ArrayList<String> music_authors;
    ArrayList<String> music_durations;
    ArrayList<File> files;
    ArrayList<UploadInfo> infos=new ArrayList<UploadInfo>();
    public UploadInfos(ArrayList<String> music_titles, ArrayList<String> music_authors, ArrayList<String> music_durations, ArrayList<File> files){
        this.files=files;
        this.music_authors=music_authors;
        this.music_titles=music_titles;
        this.music_durations=music_durations;
    }


    public  ArrayList<UploadInfo> getUploadInfos(){
        if(files.size()==music_titles.size()&&music_titles.size()==music_authors.size()){
            UploadInfo info_;
            for(int i=0;i<files.size();i++){
                info_ =new UploadInfo();
                info_.file=files.get(i);
                info_.music_author=music_authors.get(i);
                info_.music_duration=music_durations.get(i);
                Log.w("author2",Boolean.toString(music_authors.get(i).contentEquals("<unknown>")));
                info_.music_title=music_titles.get(i);
                Log.w("title2",music_titles.get(0));
                infos.add(info_);
            }
        }
     return infos;
    }
}
