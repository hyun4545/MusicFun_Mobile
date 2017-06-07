package com.example.myungjong.musicfun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myungjong.musicfun.Activity.MainActivity;
import com.example.myungjong.musicfun.R;

import java.util.ArrayList;

/**
 * Created by myungjong on 2016/7/15.
 */
public class UploadAdapter extends BaseAdapter {
    ArrayList<String> arr;
    int process_item=0;
    int process=0;
    final int DEFAULT=0;
    int status=DEFAULT;
    public static final int UPLOADING_STATUS=1;
    public static final int ERROR_STATUS=2;
    public static final int UPLOADED=3;
    final Context ctx;

    public UploadAdapter(Context context,ArrayList<String> arr){
       this.arr=arr;
        this.ctx=context;
    }
    public void setProcess(int process){
        this.process=process;
        notifyDataSetChanged();
    }

public void addItem(ArrayList<String> arr){
    this.arr=arr;
    notifyDataSetChanged();
}

    public void changeStatus(int status){
        this.status=status;
        notifyDataSetChanged();
    }
    public void removeItem(int position){
        arr.remove(position);
       //process_item++;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView=inflater.inflate(R.layout.listitem3,parent,false);
        TextView tv_music_name,tv_process;
        tv_music_name= (TextView) convertView.findViewById(R.id.textView4);
        tv_process= (TextView) convertView.findViewById(R.id.textView5);
        tv_music_name.setText(arr.get(position).toString());
        if(position==process_item){
            switch (status){
                case UPLOADING_STATUS:
                    tv_process.setText("上傳中...");
                    break;
                case UPLOADED:
                    tv_process.setText("上傳完成");
                    break;
                case ERROR_STATUS:
                    tv_process.setText("上傳失敗");
                    break;
            }
        }else {
            tv_process.setText("準備上傳");
        }



        //tv_process.setText("上傳中...");
        return convertView;
    }
}
