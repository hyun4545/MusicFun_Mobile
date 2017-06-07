package com.example.myungjong.musicfun.Adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;



import android.app.Fragment.*;

import android.os.Parcelable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.myungjong.musicfun.Activity.MainActivity;
import com.example.myungjong.musicfun.Fragment.*;

import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myungjong on 2016/5/24.
 */
public class SectionsPagerAdapter  extends FragmentStatePagerAdapter {
    MainActivity mainActivity;
    List<MusicList> musicList;
    Bundle bundle;
    public SectionsPagerAdapter(FragmentManager fm, Bundle bundle) {
         super(fm);
        this.bundle=bundle;

    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                MusicFragment mf=new  MusicFragment();
                Bundle bundle=new Bundle();
                bundle.putString("LIST_URI","ListMusic");
                mf.setArguments(bundle);
                return mf;
            case 1:
                return new ListFragment();
            case 2:
                UploadFragment fragment=new UploadFragment();
               // fragment.setArguments(bundle);
                return fragment;
            case 3:

                // fragment.setArguments(bundle);
                return new SongListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
