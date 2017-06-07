package com.example.myungjong.musicfun.Adapter;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Fragment.*;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.SongList;
import com.example.myungjong.musicfun.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by myungjong on 2017/3/2.
 */

public class MusicListAdapter extends BaseAdapter {
    ArrayList<SongList> songList;
    ListMainFragment listMainFragment;
    MyApplication application;
    public MusicListAdapter(ArrayList<SongList> songList,ListMainFragment ctx){
        this.songList=songList;
        this.listMainFragment=ctx;
        this.application= (MyApplication) listMainFragment.getActivity().getApplication();
    }
    @Override
    public int getCount() {
        return songList.size()+3;
    }

    @Override
    public SongList getItem(int position) {
        return songList.get(position-3);
    }

    @Override
    public long getItemId(int position) {
        return songList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = listMainFragment.getActivity().getLayoutInflater();
            convertView=inflater.inflate(R.layout.songlist_item,null);
            viewHolder=new ViewHolder();
            viewHolder.list_item= (LinearLayout) convertView.findViewById(R.id.songList_item);
            viewHolder.list_name= (TextView) convertView.findViewById(R.id.list_name);
            viewHolder.swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.swipe_layout);
            viewHolder.delete_list= (Button) convertView.findViewById(R.id.delete_list);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView5);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        if(position<3){
            String s="";
            String url="";
            switch (position){
                case 0: s="最近播放"; url="CurrentList";
                    break;
                case 1: s="最多播放"; url="AlwaysList";
                    break;
                case 2: s="最近新增"; url="LatestList";
                    break;

            }
            viewHolder.list_name.setText(s);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            final String finalUrl = url;
            viewHolder.list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction trans = listMainFragment.getFragmentManager()
                            .beginTransaction();
                    MusicFragment mFrag = new MusicFragment();
                    Bundle data=new Bundle();
                    data.putString("LIST_URI", finalUrl);
                    mFrag.setArguments(data);
                    trans.replace(R.id.fragment_list, mFrag);
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            });
        }else {
        viewHolder.delete_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(listMainFragment.getActivity());
                builder.setMessage("確定要刪除\""+getItem(position).getList_name()+"\"?");
                builder.setPositiveButton("確定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listMainFragment.deleteList((int)getItemId(position));

                    }
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
//set show mode.
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, convertView.findViewById(R.id.bottom_wrapper));

        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.e("swipe","show");
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
        viewHolder.list_name.setText(getItem(position).getList_name());
        viewHolder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = listMainFragment.getFragmentManager()
                        .beginTransaction();
                MusicFragment mFrag = new MusicFragment();
                Bundle data=new Bundle();
                data.putInt("list_id",(int)getItemId(position-3));
                data.putString("LIST_URI","ListSong");
                mFrag.setArguments(data);
                trans.replace(R.id.fragment_list, mFrag);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        }
        return convertView;
    }
    static class ViewHolder{
        TextView list_name;
        LinearLayout list_item;
        SwipeLayout swipeLayout;
        Button delete_list;
        ImageView imageView;
    }
}
