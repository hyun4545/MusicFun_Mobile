package com.example.myungjong.musicfun.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.myungjong.musicfun.Adapter.MusicListAdapter;
import com.example.myungjong.musicfun.Helper.MusicInfo;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjong.musicfun.Activity.MainActivity;
import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Adapter.*;
import com.example.myungjong.musicfun.Helper.MusicFragmentCallback;
import com.example.myungjong.musicfun.Helper.MusicFun;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.Model.SongList;
import com.example.myungjong.musicfun.R;
import com.example.myungjong.musicfun.Service.MusicService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;

/**
 * Created by myungjong on 2016/9/26.
 */
public class MusicFragment extends Fragment implements MusicFragmentCallback {
    MainActivity activity;
    ListView listView;
    MyApplication application;
    ListAdapter listAdapter;
    TextView search_text,null_text;
    int last_id;
    int position=-1;
    boolean isFilter;
    ProgressBar list_load;
    List<MusicList> musicLists;
    SharedPreferences sharedPreferences;
    String token;
    //String uri="http://webapplication5020160822045658.azurewebsites.net/api/Account/";
    ProgressDialog progressDialog;
    EditText song_title,song_author,addList_name;
    String list_uri;
    MusicService musicService;
    MusicFun musicFun;
    View rootView;
    String uri;
    ArrayList<SongList> musicList;
    BlurView dialog;
    ViewGroup root;
    LinearLayout frameLayout;
    int list_id=-1;
    Dialog list_dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_songs,container,false);
        root = container;
        activity= (MainActivity) getActivity();
        application= (MyApplication) activity.getApplication();
        FitViewByID(rootView);
        registerForContextMenu(listView);
        sharedPreferences=activity.getSharedPreferences("token", Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token","");
        SetProgress();
        list_uri=getArguments().getString("LIST_URI");
        list_id=getArguments().getInt("list_id");
        uri=MyApplication.uri;
        /*musicLists=getArguments().getParcelableArrayList("musicList");
        if(musicLists!=null){
            list_load.setVisibility(View.INVISIBLE);
            application.setMusicLists(musicLists);
            listAdapter=new ListAdapter(getActivity(),musicLists,MusicFragment.this);
            listView.setAdapter(listAdapter);
        }*/


        if(token!=null&&list_uri!=null){
            Log.w("SongFragment","notNull");
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Message message=Message.obtain();
                    List<MusicList> musicLists;
                    Log.e("list_id",Integer.toString(list_id));
                    switch (list_uri){
                        case "ListMusic":
                            UrlConnect<MusicList> conn=new UrlConnect<MusicList>(uri+list_uri,"GET",null,token);
                            musicLists=conn.endToConnect1(MusicList.class);
                            message.obj=musicLists;
                            break;
                        case "ListSong":
                            UrlConnect<MusicList> connect=new UrlConnect<MusicList>(uri+"GetListSong","POST",UrlConnect.FORM_DATA,token);
                            connect.setFormData("list_id",Integer.toString(list_id));
                            musicLists=connect.endToConnect1(MusicList.class);
                            message.obj=musicLists;
                            break;
                        default:
                            UrlConnect<MusicList> conn2=new UrlConnect<MusicList>(uri+list_uri,"GET",null,token);
                            musicLists=conn2.endToConnect1(MusicList.class);
                            message.obj=musicLists;
                            break;
                    }
                    mHandle.sendMessage(message);

                }
            }.start();
        }

        activity.getSearchView().setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("newText2",query);
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("newText",newText);
                if(listAdapter!=null){
                    listAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(activity.getMenuItem(), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //setFilter(false);
                Log.w("onMenuItemCollapse","1234");
               //getMusicInfo().setMusicLists(application.getOriginal_musicLists());
                adapterChange();
                listAdapter.setIsSearch(false);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                setFilter(true);
                return true;  // Return true to expand action view
            }
        });
        return rootView;
    }
    public void adapterChange(){
        Log.e("listAdapter==null",Boolean.toString(listAdapter==null));
        if(listAdapter!=null){
            listAdapter.notifyDataSetChanged();
       if(getMusicInfo().getPosition()!=-1){
           listView.setSelection(getMusicInfo().getPosition());
       }
        }
    }

    private MusicInfo getMusicInfo(){
        return MusicInfo.getInstance();
    }
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            musicLists= (List<MusicList>) msg.obj;
            Log.e("yaay",Boolean.toString(musicLists==null));
            if(musicLists!=null&&musicLists.size()>0){
                list_load.setVisibility(View.INVISIBLE);
                    //getMusicInfo().setMusicLists(musicLists);
                    application.setOriginal_musicLists(musicLists);
                listAdapter=new ListAdapter(activity,musicLists,MusicFragment.this);
                listView.setAdapter(listAdapter);
            }else {
                list_load.setVisibility(View.INVISIBLE);
                null_text.setVisibility(View.VISIBLE);
            }
        }
    };
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
    private void refreshFragment(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
public void setEditDialog(final MusicList musicList){
    final Dialog dialog = new Dialog(getActivity());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.edit_dailog);
    TextView title= (TextView) dialog.findViewById(R.id.song_title);
    title.setText(musicList.getSong_title());
    LinearLayout edit,delete,addList;
    edit= (LinearLayout) dialog.findViewById(R.id.edit_edit);
    edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setModifyDialog(musicList);
            dialog.dismiss();
        }
    });
    delete= (LinearLayout) dialog.findViewById(R.id.edit_delete);
    delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Looper.prepare();
                    handler.sendEmptyMessage(2);
                    UrlConnect conn=new UrlConnect(uri+"Delete/"+Integer.toString(musicList.getId()),"GET",null,token);
                    int responcod=conn.endToConnect2();
                    progressDialog.dismiss();
                    if(responcod!=200){

                        Toast.makeText(getActivity(),"刪除失敗!",Toast.LENGTH_SHORT).show();

                    }else {
                        handler.sendEmptyMessage(1);
                    }
                    Looper.loop();
                }
            }.start();
            dialog.dismiss();

        }
    });
    addList= (LinearLayout) dialog.findViewById(R.id.edit_addList);
    addList.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getMusicList(musicList.getId());
            dialog.dismiss();

        }
    });
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
}
    public void addSongToList(final int list_id, final int song_id){
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        progressDialog.dismiss();
                        Toast.makeText(MusicFragment.this.getActivity(),"歌曲加入清單失敗!",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        progressDialog.dismiss();
                        Toast.makeText(MusicFragment.this.getActivity(),"歌曲成功加入清單!",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        list_dialog.dismiss();
                        progressDialog.show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        new  Thread(){
            @Override
            public void run() {
                handler.sendEmptyMessage(2);
                UrlConnect conn=new UrlConnect(application.uri+"AddListSong","POST",UrlConnect.FORM_DATA,application.getToken());
                conn.setFormData("list_id",Integer.toString(list_id));
                conn.setFormData("song_id",Integer.toString(song_id));
                int response=conn.endToConnect2();
                if(response!=200){
                    handler.sendEmptyMessage(0);
                }else {
                    handler.sendEmptyMessage(1);
                }
                super.run();
            }
        }.start();
    }

    private void getMusicList(final int song_id){
        final Handler listHhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                musicList= (ArrayList<SongList>) msg.obj;
                list_dialog = new Dialog(getActivity());
                list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                list_dialog.setContentView(R.layout.list_song_dialog);
                ListView listView= (ListView) list_dialog.findViewById(R.id.addSongList);

                listView.setAdapter(new ListDialogAdapter(musicList,MusicFragment.this,song_id));
                list_dialog.show();
                super.handleMessage(msg);
            }
        };
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


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.song_menu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int itemID = info.position;
        menu.setHeaderTitle(getMusicInfo().getMusicLists().get(itemID).getSong_title());
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        final int id=getMusicInfo().getMusicLists().get(position).getId();
        switch(item.getItemId()){
            case R.id.delete:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Looper.prepare();
                        handler.sendEmptyMessage(2);
                        UrlConnect conn=new UrlConnect(uri+"Delete/"+Integer.toString(id),"GET",null,token);
                        int responcod=conn.endToConnect2();
                        progressDialog.dismiss();
                        if(responcod!=200){

                            Toast.makeText(getActivity(),"刪除失敗!",Toast.LENGTH_SHORT).show();

                        }else {
                            handler.sendEmptyMessage(1);
                        }
                        Looper.loop();
                    }
                }.start();

                break;
            case R.id.modify:
               // setModifyDialog(position,id);
                //triggerDialog();
                break;
        }
        return true;
    }

    private void setModifyDialog(final MusicList musicList){
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View layout=inflater.inflate(R.layout.modify_dialog,(ViewGroup)getActivity().findViewById(R.id.dialog_root));
        song_title= (EditText) layout.findViewById(R.id.et_title);
        song_author= (EditText) layout.findViewById(R.id.et_author);
        song_title.setText(musicList.getSong_title());
        song_author.setText(musicList.getAuthor());
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
                        handler.sendEmptyMessage(0);
                        UrlConnect conn=new UrlConnect(uri+"Modify/"+Integer.toString(musicList.getId()),"POST",UrlConnect.FORM_DATA,token);
                        String author="";
                        String title="";
                        try {
                            author= URLEncoder.encode(song_author.getText().toString(),"UTF-8").replace("+", "%20");
                            title= URLEncoder.encode(song_title.getText().toString(),"UTF-8").replace("+", "%20");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        conn.setFormData("author",author);
                        conn.setFormData("song_name",title);
                        Log.e("song_title",song_title.getText().toString());
                        int responcod=conn.endToConnect2();
                        progressDialog.dismiss();
                        if(responcod!=200){

                            Toast.makeText(getActivity(),"修改失敗!",Toast.LENGTH_SHORT).show();

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

    private void SetProgress(){
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    public void setFilter(String newText){
        listAdapter.getFilter().filter(newText);
    }

    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            listAdapter.notifyDataSetChanged();
        }
    };
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    progressDialog.setMessage("修改中...");
                    progressDialog.show();
                    break;
                case 1:
                    refreshFragment();
                    break;
                case 2:
                    progressDialog.setMessage("刪除中...");
                    progressDialog.show();
                    break;
                case 3:
                    progressDialog.setMessage("中...");
                    progressDialog.show();
                    break;
            }
        }
    };
    public void setSearch_text(int count){
        if(count==0){
            search_text.setVisibility(View.VISIBLE);
        }else {
            search_text.setVisibility(View.INVISIBLE);
        }
    }


    private void FitViewByID(View rootView) {
        listView= (ListView) rootView.findViewById(R.id.listView3);
        search_text= (TextView) rootView.findViewById(R.id.search_text);
        list_load= (ProgressBar) rootView.findViewById(R.id.list_loader);
        listView= (ListView) rootView.findViewById(R.id.listView3);
        null_text= (TextView) rootView.findViewById(R.id.null_text);
    }
    @Override
    public void onStart() {

            adapterChange();

        super.onStart();
    }


    public boolean isFilter() {
        return isFilter;
    }

    public void setFilter(boolean filter) {
        isFilter = filter;
    }

    /*public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }*/

    @Override
    public void refreshAdapter() {
        if(listAdapter!=null){
            listAdapter.notifyDataSetChanged();
        }
    }
}
