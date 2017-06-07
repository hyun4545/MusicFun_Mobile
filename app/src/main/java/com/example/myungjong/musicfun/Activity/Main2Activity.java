package com.example.myungjong.musicfun.Activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myungjong.musicfun.Adapter.MusicAdapter;
import com.example.myungjong.musicfun.Model.MusicListInfo;
import com.example.myungjong.musicfun.R;

import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_EXTERNAL_STORAGE =0 ;
    MusicAdapter adapter;
    ListView listView;
    ArrayList<MusicListInfo> music_list;
    ImageButton all_select;
    Boolean isChecked=false;
    TextView search_text,show_text,select_count;
    LinearLayout select_all;
    //final ActionBar actionBar = getActionBar();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main2);
        bindViewInID();

        Log.w("Main2","onCreate");
        int permission = ActivityCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 無權限，向使用者請求
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );

        }else{
            music_list=getMusicList();
            if(music_list.size()==0){
                show_text.setVisibility(View.VISIBLE);
                select_all.setVisibility(View.INVISIBLE);
            }else {
                adapter=new MusicAdapter(music_list,this);
                listView.setAdapter(adapter);
            }
        }

        SearchView search = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.w("CHANGE","1234");
                adapter.getFilter().filter(newText);
                return true;
            }
        });






        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.upload:
                       /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        UploadFragment fragment = new UploadFragment();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("files_ids",adapter.getArrList());
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.fragment_uplaod, fragment);
                        transaction.commit();*/
                        Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                        intent.putStringArrayListExtra("files_ids",adapter.getArrList());
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //intent.putStringArrayListExtra("files_ids",adapter.getArrList());
                       startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }
    private SearchView.OnQueryTextListener queryListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {

            //直接丟給filter

            adapter.getFilter().filter(newText);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d("TAG", "submit"+query);
            return true;
        }
    };


    public void setSearch_text(int count){
        if(count==0){
            search_text.setVisibility(View.VISIBLE);
        }else {
            search_text.setVisibility(View.INVISIBLE);
        }
       // select_all.setVisibility(View.INVISIBLE);
    }
    public void setToolBarTitle(String text){
        select_count.setText(text);
        select_count.setVisibility(View.VISIBLE);
        toolbar.setTitle(text);
    }
    private void bindViewInID(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        search_text= (TextView) findViewById(R.id.upload_search_text);
        show_text= (TextView) findViewById(R.id.upload_show_text);
        select_all= (LinearLayout) findViewById(R.id.select_all_box);
        select_count= (TextView) findViewById(R.id.select_count);
        listView= (ListView) findViewById(R.id.listView);
        all_select= (ImageButton) findViewById(R.id.all_select);
        toolbar.inflateMenu(R.menu.main2);
        all_select.setOnClickListener(this);
    }
    @Override
    public void onClick(View arg0) {
        if(arg0==all_select)  {
            isChecked=isChecked?false:true;
            if(isChecked){
                Drawable d= ContextCompat.getDrawable(this, R.drawable.ic_check_circle_black_24px);
               all_select.setImageDrawable(d);
                for (MusicListInfo info:music_list
                     ) {
                    adapter.arr_list.add(info._id);
                    info.isSelected=true;
                }
                adapter.notifyDataSetChanged();
                toolbar.setTitle("selected "+music_list.size());
                adapter.select_count=music_list.size();
            }else {
                Drawable d= ContextCompat.getDrawable(this, R.drawable.ic_radio_button_unchecked_black_24px);
                all_select.setImageDrawable(d);
                for (MusicListInfo info:music_list
                        ) {
                    info.isSelected=false;
                }
                adapter.notifyDataSetChanged();
                toolbar.setTitle("selected 0");
                adapter.select_count=0;
                adapter.arr_list=null;
            }
            //adapter.notifyDataSetChanged();
     // adapter.allClick(isChecked);


        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //取得權限，進行檔案存取
                    music_list=getMusicList();
                    if(music_list.size()==0){
                        show_text.setVisibility(View.VISIBLE);
                        select_all.setVisibility(View.INVISIBLE);
                    }else {
                        adapter=new MusicAdapter(music_list,this);
                        listView.setAdapter(adapter);
                    }
                } else {
                    //使用者拒絕權限，停用檔案存取功能
                    music_list=new ArrayList<>();
                }
                return;
        }
    }
    private  ArrayList<MusicListInfo> getMusicList() {

        ArrayList<MusicListInfo> arr = new ArrayList<MusicListInfo>();
        String[] column = { MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID };

        Cursor cursor = getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        column, null, null, "LOWER("+ MediaStore.Audio.Media.TITLE+") ASC");
        int count = cursor.getCount();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int columnIndex2 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        int columnIndex3 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        if (count>0&&cursor != null) {
            cursor.moveToFirst();
            do {

               MusicListInfo music_info=new MusicListInfo();
               music_info.file_path= cursor.getString(columnIndex2);
               music_info._id= cursor.getString(columnIndex);
               music_info.title=cursor.getString(columnIndex3);
                music_info.isSelected=false;
               arr.add(music_info);

                Log.w("uri33", cursor.getString(columnIndex3));
                //cursor.moveToNext();
            }while(cursor.moveToNext());
        }

        cursor.close();
        Log.w("uri55", arr.get(2).title);
       return  arr;
    }
    /*public ActionBar getActionBar(){
        return actionBar;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);



            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        Log.w("manger",Boolean.toString(manager==null));

            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        Log.w("search",Boolean.toString(search==null));
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.w("CHANGE","1234");
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
          case R.id.upload:

              UploadFragment fragment = new UploadFragment();
              Bundle bundle = new Bundle();
              bundle.putStringArrayList("files_ids",adapter.getArrList());
              fragment.setArguments(bundle);
                Intent intent=new Intent(this,MainActivity.class);
                //intent.putStringArrayListExtra("files_ids",adapter.getArrList());
               startActivity(intent);
              finish();
               // Bundle bundle=new Bundle();
                //bundle.putParcelableArrayList(adapter.arr_list);
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onStop() {
        Log.w("Main2","onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.w("Main2","onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.w("Main2","onPause");
        super.onPause();
    }
}
