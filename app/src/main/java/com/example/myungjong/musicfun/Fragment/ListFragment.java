package com.example.myungjong.musicfun.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjong.musicfun.Activity.MainActivity;
import com.example.myungjong.musicfun.Adapter.GridListAdapter;
import com.example.myungjong.musicfun.Adapter.ListAdapter;
import com.example.myungjong.musicfun.Adapter.MusicListAdapter;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class ListFragment extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_list, new ListMainFragment());

            transaction.commit();

            return rootView;

        }



}
