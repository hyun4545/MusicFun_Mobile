package com.example.myungjong.musicfun.Model;

/**
 * Created by myungjong on 2017/3/2.
 */

public class SongList {
    private int Id;
    private String user_id;
    private String list_name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
