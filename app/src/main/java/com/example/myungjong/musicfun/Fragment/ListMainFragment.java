package com.example.myungjong.musicfun.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Adapter.GridListAdapter;
import com.example.myungjong.musicfun.Adapter.MusicListAdapter;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.Model.SongList;
import com.example.myungjong.musicfun.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class ListMainFragment extends Fragment {
    View rootView;
    ListView songList;
    Button addListBTN;
    EditText addList_name;
    MyApplication application;
    ArrayList<SongList> musicList;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        fitViewByID();
        SetProgress();
        application= (MyApplication) getActivity().getApplication();
        getMusicList();
        addListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddListDialog();
            }
        });
        return rootView;
    }
    private void setAddListDialog() {
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View layout=inflater.inflate(R.layout.add_list_dialog,(ViewGroup)getActivity().findViewById(R.id.dialog_root2));
        addList_name= (EditText) layout.findViewById(R.id.addList_name);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Looper.prepare();
                        handler.sendEmptyMessage(3);
                        UrlConnect conn=new UrlConnect(MyApplication.uri+"AddList","POST",UrlConnect.FORM_DATA,application.getToken());
                        String list_name="";
                        try {
                            list_name= URLEncoder.encode(addList_name.getText().toString(),"UTF-8").replace("+", "%20");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        conn.setFormData("list_name",list_name);
                        int responcod=conn.endToConnect2();
                        progressDialog.dismiss();
                        if(responcod!=200){

                            Toast.makeText(getActivity(),"加入失敗!",Toast.LENGTH_SHORT).show();

                        }else {
                            handler.sendEmptyMessage(1);
                        }
                        Looper.loop();
                    }
                }.start();
            }
        });
        builder.create().show();
    }
    private void refreshFragment(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case 1:
                    refreshFragment();
                    break;
                case 3:
                    progressDialog.setMessage("加入中...");
                    progressDialog.show();
                    break;
            }
        }
    };
    public void deleteList(final int list_id){
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){

                    case 1:
                        progressDialog.dismiss();
                        refreshFragment();
                        break;
                    case 0:
                        Toast.makeText(getActivity(),"刪除失敗!",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        progressDialog.setMessage("刪除中...");
                        progressDialog.show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        new Thread(){
            @Override
            public void run() {
                handler.sendEmptyMessage(2);
                UrlConnect conn=new UrlConnect(application.uri+"DeleteList","POST",UrlConnect.FORM_DATA,application.getToken());
                conn.setFormData("list_id",Integer.toString(list_id));
                int response=conn.endToConnect2();
                if(response==200){
                    handler.sendEmptyMessage(1);
                }else {
                    handler.sendEmptyMessage(0);
                }

                super.run();
            }
        }.start();
    }
    private void SetProgress(){
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }
    private void fitViewByID(){
        songList= (ListView) rootView.findViewById(R.id.songList);
        addListBTN= (Button) rootView.findViewById(R.id.addList);
        progressBar= (ProgressBar) rootView.findViewById(R.id.list_loader);
    }
    private Handler listHhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            musicList= (ArrayList<SongList>) msg.obj;
            Log.e("IsmusicList",Boolean.toString(musicList==null));
            Log.e("IssongList",Boolean.toString(songList==null));
            Log.e("musicListC",Integer.toString(musicList.size()));
            progressBar.setVisibility(View.INVISIBLE);
            songList.setAdapter(new MusicListAdapter(musicList,ListMainFragment.this));
            super.handleMessage(msg);
        }
    };
    private void getMusicList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                UrlConnect<SongList> conn=new UrlConnect(application.uri+"MusicLists","GET",null,application.getToken());
                ArrayList<SongList> musicList=conn.endToConnect1(SongList.class);
                Message message=Message.obtain();
                message.obj=musicList;
                listHhandler.sendMessage(message);
                Looper.loop();
            }
        }.start();
    }
}
