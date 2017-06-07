package com.example.myungjong.musicfun.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by myungjong on 2016/9/1.
 */
public class MusicList implements Parcelable {
   int Id;
   String user_id;
   String song_title;
   String author;
   String file_name;
    String song_time;
    private boolean isSelect=false;
    private boolean isPlay=false;
//int origin_index=-1;

    /*public int getOrigin_index() {
        return origin_index;
    }

    public void setOrigin_index(int origin_index) {
        this.origin_index = origin_index;
    }*/

    protected MusicList(Parcel in) {
        Id = in.readInt();
        user_id = in.readString();
        song_title = in.readString();
        author = in.readString();
        file_name = in.readString();
        song_time = in.readString();
        isSelect = in.readByte() != 0;
        isPlay = in.readByte() != 0;
    }

    public static final Creator<MusicList> CREATOR = new Creator<MusicList>() {
        @Override
        public MusicList createFromParcel(Parcel in) {
            return new MusicList(in);
        }

        @Override
        public MusicList[] newArray(int size) {
            return new MusicList[size];
        }
    };

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getSong_time() {
        return song_time;
    }

    public void setSong_time(String song_time) {
        this.song_time = song_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(user_id);
        dest.writeString(song_title);
        dest.writeString(author);
        dest.writeString(file_name);
        dest.writeString(song_time);
    }
}
