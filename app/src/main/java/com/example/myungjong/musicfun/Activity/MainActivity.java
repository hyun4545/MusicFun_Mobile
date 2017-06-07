package com.example.myungjong.musicfun.Activity;

/**
 * Created by myungjong on 2016/5/10.
 */

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myungjong.musicfun.Adapter.SectionsPagerAdapter;
import com.example.myungjong.musicfun.Fragment.*;


import com.example.myungjong.musicfun.Helper.BindHelper;
import com.example.myungjong.musicfun.Helper.GetFragmentHelper;
import com.example.myungjong.musicfun.Helper.MusicFragmentCallback;
import com.example.myungjong.musicfun.Helper.MusicFun;
import com.example.myungjong.musicfun.Helper.MusicInfo;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.Callback;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.Model.UploadInfo;
import com.example.myungjong.musicfun.Model.UploadInfos;
import com.example.myungjong.musicfun.R;
import com.example.myungjong.musicfun.Service.MusicService;
import com.example.myungjong.musicfun.Service.UploadService;
import com.example.playerbtn.PlayerImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MusicFragmentCallback,Button.OnClickListener {
    private ViewPager mViewPager;
    ProgressBar progressBar;
  //  ImageButton btn_play,btn_pause;
    ImageView imageView;
    private int mMax; // MediaPlayer 音樂總時間
    private int sMax; // SeekBar 最大值
    TabLayout tabLayout;
    TextView song_title,search_text,song_author;
    Toolbar toolbar;
    LinearLayout bottom_layout;
    ArrayList<File> files=new ArrayList<>();
    DrawerLayout drawer;
    NavigationView navigationView;
    ProgressBar song_loader,list_loader;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ArrayList<String> files_ids;
    UploadFragment fragment = new UploadFragment();
    ArrayList<String> music_titles = new ArrayList<String>();
    ArrayList<String> music_authors=new ArrayList<String>();
    ArrayList<String> music_durations=new ArrayList<String>();
    SharedPreferences sharedPreferences;
    String token;
    //String uri="http://webapplication5020160822045658.azurewebsites.net/api/Account/ListMusic";
    List<MusicList> musicLists;
    MyApplication application;
    MusicFragment musicFragment;
    SearchView search;
    Thread thread;
    int music_duration;
    LayoutInflater inflater;
    MusicService musicService;
    UploadService uploadService;
    SongsFragment songFragment;
    int position;
    MenuItem menuItem;
    boolean isFilter;
    MusicFun musicFun;
   ViewGroup root,root2;
    PlayerImageView playerImageView;
    BlurView bottomBlurLayout,tabBlurLayout,drawerBlurView;
    private BindHelper<MusicService,MusicService.ServiceBinder> playerbindHelper;
    private BindHelper<UploadService,UploadService.ServiceBinder>uploadbindHelper;
    private static ArrayList<Handler> handlers=new ArrayList<Handler>();

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFilter() {
        return isFilter;
    }

    /*public void setFilter(boolean filter) {
        isFilter = filter;
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.e("savedInstanceState",Boolean.toString(savedInstanceState==null));
        setContentView(R.layout.activity_main);


        Log.w("MainActivity","onCreate");
        application= (MyApplication) this.getApplication();

        MatchViewById();
        bindMusicService();
        bindUploadService();
       setActionBarTitle();
        setTabSelect();
        setBarDrawer();

        final Drawable windowBackground = getWindow().getDecorView().getBackground();

        final BlurView.ControllerSettings topViewSettings = tabBlurLayout.setupWith(root)
                .windowBackground(windowBackground)
                .blurRadius(5f);





        song_loader.setVisibility(View.INVISIBLE);
        musicFragment= (MusicFragment)  mSectionsPagerAdapter.instantiateItem(mViewPager,0);
        //Log.e("registerevent is null",Boolean.toString(application.registerEvent==null));


        Log.e("musicService is null",Boolean.toString(musicService==null));




        Log.w("ffr5",Boolean.toString(musicService==null));
       // setMusicBar();
//if(musicService==null){


//}bindUploadService()

        playerImageView.setPlayerClickListener(new PlayerImageView.PlayerClickListener(){
            @Override
            public void play() {
                startMusic();
            }

            @Override
            public void pause() {
                pauseMusic();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("music33", String.valueOf(null==null));
              //UnbindService(MUSICSERVICE);
               Intent intent=new Intent(MainActivity.this,PlayActivity.class);
                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView,getString(R.string.transitionName));
               ActivityCompat.startActivity(MainActivity.this,intent,optionsCompat.toBundle());
                song_loader.setVisibility(View.INVISIBLE);

            }
        });

      /*UploadFragment fragment =new UploadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("files_id","files_ids");
        fragment.setArguments(bundle);*/


    }
   /* public void hideListProgress(){
        list_loader.setVisibility(View.INVISIBLE);
    }*/
    /*private void setPlayer(){
        if(musicFun.isPlay()){
            Log.w("play","still play!!");
            btn_play.setVisibility(View.VISIBLE);
            btn_pause.setVisibility(View.INVISIBLE);
        }else {
            btn_play.setVisibility(View.INVISIBLE);
            btn_pause.setVisibility(View.VISIBLE);
        }
    }*/


public void getWindow1(){
    application.windoww=getWindow().getDecorView().getBackground();
}
    /*public void setMusicBar() {

        if(music_isBound&&musicService!=null){
            if(musicFun.isPlayerExisted()){

                //Log.e("000000",application.getTitle());
                setPlayer();
                startProgressUpdate();
            }

        }else {
            Log.w("music_isBound",Boolean.toString(music_isBound));
            Log.w("musicService!=null",Boolean.toString(musicService!=null));

            //Log.w("isPlayerExisted",Boolean.toString(musicService.isPlayerExisted()));
            btn_play.setVisibility(View.INVISIBLE);
            btn_pause.setVisibility(View.VISIBLE);
        }
    }*/
    private MusicInfo getMusicInfo(){
       return MusicInfo.getInstance();
    }
public void setBottom_layout(){
    Log.e("setBottom_layout","setBottom_layout");

    if(bottom_layout.getVisibility() == View.GONE&&musicFun.isPlayerExisted()){
        Log.e("setBottom_layout","setBottom_layout");
        bottom_layout.setVisibility(View.VISIBLE);
        song_title.setText(getMusicInfo().getTitle());
        mMax=getMusicInfo().getDuration();
        progressBar.setMax(mMax);
        progressBar.setProgress(0);
    }


    if(musicFun.isPlay()){
        //btn_pause.setVisibility(View.INVISIBLE);
        //btn_play.setVisibility(View.VISIBLE);
    }else {
        //btn_pause.setVisibility(View.VISIBLE);
        //btn_play.setVisibility(View.INVISIBLE);
    }

}
    private void setBarDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        final TextView tv_user_name= (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String name= (String) msg.obj;
                tv_user_name.setText("Hi!  "+name);
                super.handleMessage(msg);
            }
        };
        new Thread(){
            @Override
            public void run() {
                UrlConnect conn=new UrlConnect(application.uri+"getUser","GET",null,application.getToken());
                String user_name=conn.endToConnect4();
                Message message=Message.obtain();
                message.obj=user_name;
                handler.sendMessage(message);
                super.run();
            }
        }.start();
    }
public  void  changeFragment(){
    Fragment mFragment = new MusicFragment();

    Fragment mFragment2 = new ListFragment();
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    //Replacing using the id of the container and not the fragment itself
    //ft.detach(mFragment2);
    //getSupportFragmentManager().popBackStack();

    ft.replace(android.R.id.tabcontent,  mFragment);
    ft.addToBackStack(null);
//ft.detach(mFragment2);
    Log.e("222222","1234");

    ft.commit();
    ;
}

    public void bindUploadService(){
        if(application.getUploadService()==null){
            Callback<UploadService> uploadServiceCallback=new Callback<UploadService>() {
                @Override
                public void doSomething(UploadService service) {
                    uploadService=service;
                    application.setUploadService(uploadService);
                }
            };
            uploadbindHelper=new BindHelper<UploadService,UploadService.ServiceBinder>(this,UploadService.class,uploadServiceCallback);
            uploadbindHelper.bindService();
        }
    }
    public void addMusicFunListener(MusicFun musicFun){
        musicFun.addMusicFunListener(new MusicFun.MusicFunListener() {
            @Override
            public void onProgressUpdate(int progress) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onSeekComplete() {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onPrepare() {
                Log.e("onPrepare()",getMusicInfo().getTitle());
                playerImageView.pause();
                musicFragment.adapterChange();
                song_loader.setVisibility(View.VISIBLE);
                song_title.setText(getMusicInfo().getTitle());
                song_author.setText(getMusicInfo().getAuthor());
                setBottom_layout();
            }

            @Override
            public void onPrepared() {
                song_loader.setVisibility(View.INVISIBLE);
                playerImageView.play();
                //btn_paus.e.setVisibility(View.INVISIBLE);
                //btn_play.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBuffering() {
                if(playerImageView.isPlay()){
                    playerImageView.pause();
                    song_loader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause() {
                //btn_pause.setVisibility(View.VISIBLE);
                //btn_play.setVisibility(View.INVISIBLE);
                playerImageView.pause();
            }

            @Override
            public void onStart() {
                playerImageView.play();
                // btn_pause.setVisibility(View.INVISIBLE);
                //btn_play.setVisibility(View.VISIBLE);
            }
        });
    }
    public void bindMusicService(){
        bottom_layout.setVisibility(View.GONE);
        if(application.getMusicService()==null){
            Callback<MusicService> musicServiceCallback=new Callback<MusicService>() {
                @Override
                public void doSomething(MusicService service) {
                    musicService=service;
                    application.setMusicService(musicService);
                    musicFun=musicService.musicFun;
                   addMusicFunListener(musicFun);
                    Log.e("service5",Boolean.toString(service==null));
                }
            };

            playerbindHelper=new BindHelper<MusicService,MusicService.ServiceBinder>(this,MusicService.class,musicServiceCallback);
            playerbindHelper.bindService();
        }else {
            musicFun=application.getMusicService().musicFun;
            addMusicFunListener(musicFun);
            setBottom_layout();
        }
    }
    private Fragment getFragment;
    private void setTabSelect() {

       Bundle bundle = new Bundle();
        bundle.putString("files_id","files_ids");
       mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(),bundle);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        tabLayout.addTab(tabLayout.newTab().setText("所有歌曲"),0,true);
        tabLayout.addTab(tabLayout.newTab().setText("播放清單"),1);
        tabLayout.addTab(tabLayout.newTab().setText("上傳中"),2);
       mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setActionBarTitle() {
       // setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.inflateMenu(R.menu.main);
        search = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();

        menuItem = toolbar.getMenu().findItem(R.id.action_search);

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.w("onClose","1234");
                return false;
            }
        });
        /*search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.w("onQueryTextSubmit","onQueryTextSubmit");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.w("CHANGE","1234");
                setFilter(true);
                songFragment.setFilter(newText);
                return true;
            }
        });*/
    }

    private void MatchViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //btn_pause= (ImageButton) findViewById(R.id.bottom_pause);
       // btn_play= (ImageButton) findViewById(R.id.bottom_play);
        imageView= (ImageView) findViewById(R.id.goMusic);
        song_title= (TextView) findViewById(R.id.bottom_song_name);
        song_loader= (ProgressBar) findViewById(R.id.song_loader);
        list_loader= (ProgressBar) findViewById(R.id.list_loader);
        bottom_layout= (LinearLayout) findViewById(R.id.bottom_layout);
        search_text= (TextView) findViewById(R.id.search_text);
        root= (ViewGroup) findViewById(R.id.root);
       // root2= (ViewGroup) findViewById(R.id.container_main);
       // bottomBlurLayout= (BlurView) findViewById(R.id.blurView2);
        tabBlurLayout= (BlurView) findViewById(R.id.blurView);
        playerImageView= (PlayerImageView) findViewById(R.id.player_btn);
        song_author= (TextView) findViewById(R.id.song_author);


    }

    public SearchView getSearchView(){
        return search;
    }
    public MenuItem getMenuItem(){
        return menuItem;
    }
    public void setSearch_text(int count){
        if(count==0){
            search_text.setVisibility(View.VISIBLE);
        }else {
            search_text.setVisibility(View.INVISIBLE);
        }
        // select_all.setVisibility(View.INVISIBLE);
    }
    public void setSongTitle(String title){
        song_title.setText(title);
    }
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upload) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.detach(fragment);
            transaction.commitAllowingStateLoss();
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            // intent.setType("audio/*");
            // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            // intent.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(Intent.createChooser(intent, "Select music"),1);

            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            ((MyApplication)getApplication()).deleteToken();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//開始音樂時間軸
   /* public void startProgressUpdate() {
            mMax=musicFun.getDuration();
            progressBar.setMax(application.getMusic_duration());
            progressBar.setProgress(musicFun.getProgress());

                application.getHandlers().addHandlers(mHandle);
         application.goProgress(music_isBound);

        //Log.w("count2",Integer.toString(application.handlerCount()));


    }*/
/*public void setDuration(int duration){
    application.music_duration=duration;
    music_duration=application.music_duration;
}*/



private void startMusic(){
    if(musicFun!=null){
        musicFun.start();
    }else {
        playerbindHelper.bindService();
    }

}

    private void pauseMusic(){
        if(musicFun!=null){
            musicFun.pause();
        }else {
            playerbindHelper.bindService();
        }

    }
    public void onClick(View view) {




    }
    private ArrayList<String> getInfoFromURI(ArrayList<String> files_ids) {

        ArrayList<String> file_paths = new ArrayList<String>();
        Log.w("file_id",Boolean.toString(files_ids.isEmpty()));
        String[] id_s=new String[files_ids.size()];
        int files_count=files_ids.size();
        String sel = MediaStore.Audio.Media._ID + "=?";
        for (int i=0;i<files_count;i++
                ) {
            if(i>0){
                sel+=" OR " +MediaStore.Audio.Media._ID + "=?";
            }
            id_s[i]=files_ids.get(i);
        }
        String[] column = { MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID, MediaStore.Files.FileColumns.MIME_TYPE,MediaStore.Audio.Media.DURATION};
        Log.w("filepath","1");
// where id is equal to
        // String sel = MediaStore.Audio.Media._ID + "=?";
        //String sel = MediaStore.Audio.Media._ID + "=?"/*+ " OR " +MediaStore.Audio.Media._ID + "=?"+ "OR " +MediaStore.Audio.Media._ID + "=?"*/;
        Cursor cursor = this.getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        column, sel, id_s, null);
        int count = cursor.getCount();
        Log.w("cursor_COUNT",Integer.toString(count));


        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        int columnIndex2 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        int columnIndex3 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int columnIndex4 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int columnIndex6 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int columnIndex5 = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE);
        Log.w("uri3",Integer.toString(columnIndex));
        Log.w("uri6",MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString());
        //filePath = cursor.getString(columnIndex);
        Log.w("uri4",Integer.toString(count));

        if (count>0&&cursor != null) {
            cursor.moveToFirst();
            do {

                file_paths.add(cursor.getString(columnIndex));
                music_titles.add(cursor.getString(columnIndex3));
                music_authors.add(cursor.getString(columnIndex4));
                music_durations.add(cursor.getString(columnIndex6));
                Log.w("uri22", cursor.getString(columnIndex));
                Log.w("uri33", cursor.getString(columnIndex3));
                Log.w("uri44", cursor.getString(columnIndex4));
                Log.w("uri66", cursor.getString(columnIndex6));
                Log.w("unknown",Integer.toString(columnIndex4));
               Log.w("uri55", cursor.getString(columnIndex5));
            }while(cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        /*Cursor cursor = null;
        try {
            Context ctx = MainActivity. this ;

            ContentResolver resolver = ctx.getContentResolver();

            String[] proj = { MediaStore.Audio.Media.DATA};
            cursor = resolver.query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/
        return file_paths;
    }
    private ArrayList<File> UrisToFiles(ArrayList<String> sourceFileUri){
        ArrayList<File> files=new ArrayList<File>();
        //File file;
        Log.w("fffff22",sourceFileUri.toString());
        File file;
        for (String s:sourceFileUri
                ) {
            file=new File(s);
            //s.substring(s.lastIndexOf(",") + 1).trim()
            //String ext = FilenameUtils.getExtension("/path/to/file/foo.txt");
            Log.w("fffff",file.getName());
            files.add(file);
        }
        return files;
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
       // setMusicBar();
        Log.w("MainActivity","onResume");
    }
    private String getFragmentTag(int viewPagerId, int fragmentPosition)
    {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }
    public ArrayList<String> getFiles_ids(){
        return  files_ids;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.w("MainActivity","onNewIntent");
        setIntent(intent);
        if(getIntent()!=null){
            Log.w("MainActivity","onStart2");
            if(getIntent().getStringArrayListExtra("files_ids")!=null){
                Log.w("MainActivity","getIntent");
                files_ids=getIntent().getStringArrayListExtra("files_ids");
               // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                files = UrisToFiles(getInfoFromURI(files_ids));
                Log.w("f_c",Integer.toString(files.size()));

              FragmentTransaction transaction = getFragmentManager().beginTransaction();;







               // Log.w("index",Integer.toString(mViewPager.getCurrentItem()));

               // Bundle bundle = new Bundle();
                //bundle.putStringArrayList("files_id",files_ids);
               UploadFragment fragment= (UploadFragment) getFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getId(),2));
               if(fragment==null){
                    fragment= (UploadFragment) mSectionsPagerAdapter.instantiateItem(mViewPager,2);
                   transaction.attach(fragment);
                  transaction.commitAllowingStateLoss();
               }
                fragment.setUploadAdapter(music_titles);
               // Log.w("upor",Boolean.toString(MyApplication.uploadService==null));
                UploadInfos uploadInfo=new UploadInfos(music_titles,music_authors,music_durations,files);
                ArrayList<UploadInfo> uploadInfos=uploadInfo.getUploadInfos();
                application.getUploadService().startUpload(uploadInfos,fragment.getHandler(),application.getToken());

            //transaction.detach(fragment);
           //
               //fragment.ArgOnNot=true;
           //transaction2.attach(fragment);
                //transaction.replace(R.id.container,fragment);

             //transaction.commitAllowingStateLoss();
              // tabLayout.getTabAt(2).select();
            }
        }
        super.onNewIntent(intent);

    }

    @Override
    protected void onStart() {
       // setMusicBar();

        super.onStart();
        Log.w("MainActivity","onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        //application.removeHandler(mHandle);
        //musicService.removeHandler(handler);
        Log.w("MainActivity","onStop");
    }

    @Override
    protected void onRestart() {

        Log.w("MainActivity","onRestart");
       /* if (musicService.isPlayerExisted()) {
            song_title.setText(application.getTitle());

            if(musicService.isPlayerExisted()&&!musicService.isPrepare()){
                song_loader.setVisibility(View.VISIBLE);
            }
            setMusicBar();
        }*/


        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.w("MainActivity","onDestroy");
        super.onDestroy();
    }

    @Override
    public void refreshAdapter() {

    }
   public interface MainActivityListener{

   }
}
