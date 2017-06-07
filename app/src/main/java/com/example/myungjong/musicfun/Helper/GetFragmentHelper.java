package com.example.myungjong.musicfun.Helper;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by myungjong on 2017/2/9.
 */

public class GetFragmentHelper<T extends Fragment> {
    AppCompatActivity ctx;
    T fragment;
    public GetFragmentHelper(Context ctx){
        this.ctx= (AppCompatActivity) ctx;
    }
    public T getFragment(int fragment_id){
        fragment= (T) ctx.getFragmentManager().findFragmentById(fragment_id);
        return fragment;
    }
}
