package com.example.myungjong.musicfun.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjong.musicfun.Activity.MainActivity;
import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Adapter.ListAdapter;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.R;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SongsFragment extends MusicFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    ImageButton btn_play,btn_pause;
    static LinearLayout linearLayout;
    SharedPreferences sharedPreferences;
    String token;
    String uri/*="http://webapplication5020160822045658.azurewebsites.net/api/Account/ListMusic"*/;
    List<MusicList> musicLists;
    static int last_id=-1;
    Object lock=new Object();

    LinearLayout bottom_layout;
    TextView search_text;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity= (MainActivity) getActivity();
        application= (MyApplication) activity.getApplication();
        sharedPreferences=getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token","");
        uri=MyApplication.uri+"ListMusic";
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Toast.makeText(getActivity(),token,Toast.LENGTH_LONG);
        Log.w("SongFragment","onCreateView");
        if(token!=null){
            Log.w("SongFragment","notNull");
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    UrlConnect conn=new UrlConnect(uri,"GET",null,token);
                    List<MusicList> musicLists=conn.endToConnect1(MusicList.class);
                    Message message=Message.obtain();
                    message.obj=musicLists;
                    mHandle.sendMessage(message);
                }
            }.start();
        }
        super.onActivityCreated(savedInstanceState);    }





    @Override
    public void onStop() {
        //MainActivity.musicService.removeHandler2(handler2);
        super.onStop();
    }



    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            musicLists= (List<MusicList>) msg.obj;

        }
    };


}
