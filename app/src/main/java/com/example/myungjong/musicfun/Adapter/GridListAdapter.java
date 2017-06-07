package com.example.myungjong.musicfun.Adapter;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.myungjong.musicfun.Fragment.*;
import com.example.myungjong.musicfun.Activity.MainActivity;
import com.example.myungjong.musicfun.R;

import java.util.ArrayList;

/**
 * Created by myungjong on 2016/9/26.
 */
public class GridListAdapter extends BaseAdapter {
    ArrayList<String> list=new ArrayList<String>();
    com.example.myungjong.musicfun.Fragment.ListMainFragment listFragment;
    String[] list2=new String[]{"最近播放","最近新增","最多播放","+"};
    public GridListAdapter(com.example.myungjong.musicfun.Fragment.ListMainFragment ctx){
        listFragment=ctx;

    }



    @Override
    public int getCount() {
        return list2.length;
    }

    @Override
    public Object getItem(int position) {
        return list2[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder
    {
        public TextView list_name;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder view;
        LayoutInflater inflator = listFragment.getActivity().getLayoutInflater();

        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.list_item, null);

            view.list_name = (TextView) convertView.findViewById(R.id.musiclist_item);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }
        view.list_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = listFragment.getFragmentManager()
                        .beginTransaction();
				/*
				 * IMPORTANT: We use the "root frame" defined in
				 * "root_fragment.xml" as the reference to replace fragment
				 */
                String list_uri;
                switch (position){
                    case 0:
                        list_uri="";
                        break;
                    case 1:
                        list_uri="";
                        break;
                    case 2:
                        list_uri="";
                        break;
                }
                MusicFragment mf=new  MusicFragment();
                Bundle bundle=new Bundle();
                bundle.putString("LIST_URI","ListMusic");
                mf.setArguments(bundle);
                trans.replace(R.id.fragment_list, new SongListFragment());

				/*
				 * IMPORTANT: The following lines allow us to add the fragment
				 * to the stack and return to it later, by pressing back
				 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
                //((MainActivity)listFragment.getActivity()).changeFragment();
            }
        });

        view.list_name.setText(list2[position]);
        if(position==list2.length-1){
            view.list_name.setTextSize(25);
        }
        //view.imgViewFlag.setImageResource(listFlag.get(position));

        return convertView;

    }

}
