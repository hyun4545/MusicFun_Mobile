package com.example.myungjong.musicfun.Adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Fragment.*;
import com.example.myungjong.musicfun.Helper.MusicFragmentCallback;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.SongList;
import com.example.myungjong.musicfun.R;

import java.util.ArrayList;

/**
 * Created by myungjong on 2017/3/4.
 */

public class ListDialogAdapter extends BaseAdapter {
    ArrayList<SongList> songLists;
     MusicFragment fragment;
    MyApplication application;
    int song_id;
    public ListDialogAdapter(ArrayList<SongList> songLists, Fragment fragment,int song_id){
        this.songLists=songLists;
        this.fragment= (MusicFragment) fragment;
        this.song_id=song_id;
        application= (MyApplication) fragment.getActivity().getApplication();
    }
    @Override
    public int getCount() {
        return songLists.size();
    }

    @Override
    public SongList getItem(int position) {
        return songLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return songLists.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
        if(convertView==null){
            convertView=inflater.inflate(R.layout.songlist_item2,null) ;
            viewHolder.tv= (TextView) convertView.findViewById(R.id.list_name);
            viewHolder.layout= (LinearLayout) convertView.findViewById(R.id.songList_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(getItem(position).getList_name());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            fragment.addSongToList(getItem(position).getId(),song_id);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView tv;
        LinearLayout layout;
    }
}
