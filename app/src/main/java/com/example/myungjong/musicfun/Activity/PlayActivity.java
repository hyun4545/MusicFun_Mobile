package com.example.myungjong.musicfun.Activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjong.musicfun.Helper.BindHelper;
import com.example.myungjong.musicfun.Helper.MusicFragmentCallback;
import com.example.myungjong.musicfun.Helper.MusicFun;
import com.example.myungjong.musicfun.Helper.MusicInfo;
import com.example.myungjong.musicfun.R;
import com.example.myungjong.musicfun.Service.MusicService;
import com.example.playerbtn.PlayerImageView;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton /*music_play,music_pause,*/repeat,repeat_one,
            shuffle,music_prev,music_next,prev,add_music_list;
    ImageView musicfun;
    TextView music_title,total_time,run_time;
    SeekBar seekbar;
    ProgressBar music_loader;
    private AudioManager audioManager;
    private Context context;
    private int mMax; // MediaPlayer 音樂總時間
    private int sMax; // SeekBar 最大值
    private TextView tvMessage;
    private Button btPlay, btStop;
    private boolean isBound=false;
    private MusicService musicService;
    MyApplication application;
    PlayerImageView playerImageView;
    public static final int PLAY_HANDLER_NUM=0;
    int position;
    MusicFun musicFun;
    BindHelper<MusicService,MusicService.ServiceBinder> playerbindHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Log.w("PlayActivity","onCreate");
        application= (MyApplication) getApplication();
        //playerbindHelper=new BindHelper<MusicService,MusicService.ServiceBinder>(this);
        FitViewById();

        //doBindService();

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
        musicService=application.getMusicService();
        if(musicService!=null){
            musicFun=musicService.musicFun;
            if(!musicFun.isPreprare()){
                music_loader.setVisibility(View.VISIBLE);
            }
            if(getMusicInfo().isListExit()){

                music_title.setText(getMusicInfo().getTitle());
                mMax=getMusicInfo().getDuration();
                seekbar.setMax(mMax);
                String max=getTime(mMax);
                total_time.setText(max);
                position=musicFun.getProgress();
                String now=getTime(position);
                run_time.setText(now);
                seekbar.setProgress(musicFun.getProgress());
                if(musicFun.isPlay()){
                    //music_play.setVisibility(View.VISIBLE);
                    //music_pause.setVisibility(View.INVISIBLE);
                }else {
                    //music_play.setVisibility(View.INVISIBLE);
                    //music_pause.setVisibility(View.VISIBLE);
                }
            }
        }


        initView();
        //setMusicBar();
        //application.registerEvent.addCallback(this);
          //  Log.w("not prepared",Boolean.toString(musicService.isPrepare()));
           //
       musicFun.addMusicFunListener(new MusicFun.MusicFunListener() {
           @Override
           public void onProgressUpdate(int progress) {
               seekbar.setProgress(progress);
               String now=getTime(progress);
               run_time.setText(now);
           }

           @Override
           public void onSeekComplete() {

           }

           @Override
           public void onComplete() {

           }

           @Override
           public void onPrepare() {
               Log.e("PlayActivity","onPrepare");
               music_loader.setVisibility(View.VISIBLE);
               playerImageView.pause();
               //music_play.setVisibility(View.INVISIBLE);
               //music_pause.setVisibility(View.VISIBLE);
               music_title.setText(getMusicInfo().getTitle());
               mMax=getMusicInfo().getDuration();
               seekbar.setMax(mMax);
               String max=getTime(mMax);
               total_time.setText(max);
           }

           @Override
           public void onPrepared() {
               music_loader.setVisibility(View.INVISIBLE);
               playerImageView.play();
               //music_play.setVisibility(View.VISIBLE);
               //music_pause.setVisibility(View.INVISIBLE);

           }

           @Override
           public void onBuffering() {

           }

           @Override
           public void onPause() {
               playerImageView.pause();
               //music_play.setVisibility(View.INVISIBLE);
               //music_pause.setVisibility(View.VISIBLE);
           }

           @Override
           public void onStart() {
               playerImageView.play();
              // music_play.setVisibility(View.VISIBLE);
               //music_pause.setVisibility(View.INVISIBLE);
           }
       });


    }
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
    private MusicInfo getMusicInfo(){
        return  MusicInfo.getInstance();
    }
    private void initView(){
        seekbar.setOnSeekBarChangeListener(new MyRunControlOnSeekBarChangeListener());
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        switch (application.getPlayMode()){
            case MyApplication.ALL_CYCLE:
                repeat.setVisibility(View.VISIBLE);
                shuffle.setVisibility(View.INVISIBLE);
                repeat_one.setVisibility(View.INVISIBLE);
                break;
            case MyApplication.RANDOM_CYCLE:
                shuffle.setVisibility(View.VISIBLE);
                repeat.setVisibility(View.INVISIBLE);
                repeat_one.setVisibility(View.INVISIBLE);
                break;
            case MyApplication.SINGLE_CYCLE:
                repeat_one.setVisibility(View.VISIBLE);
                repeat.setVisibility(View.INVISIBLE);
                shuffle.setVisibility(View.INVISIBLE);
                break;
        }


    }
   /*private void setMusicBar(){

       if(musicService!=null){
           Log.w("musicService", "musicService!=null");

           if(musicService.isPlayerExisted()){

               if(musicService.isPlay()){
                   music_play.setVisibility(View.INVISIBLE);
                   music_pause.setVisibility(View.VISIBLE);
                   startProgressUpdate();

               }else {
                   music_play.setVisibility(View.VISIBLE);
                   music_pause.setVisibility(View.INVISIBLE);
                   //seekbar.setProgress(musicService.getProcess());
               }

           }else {
               Log.w("nothing","nothing");
               music_play.setVisibility(View.VISIBLE);
               music_pause.setVisibility(View.INVISIBLE);
           }
       }
    }*/

    @Override
    protected void onStart() {
        if(musicFun.isPlay()){
           playerImageView.play();
            music_loader.setVisibility(View.INVISIBLE);
        }else {
            playerImageView.pause();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    private void startProgressUpdate() {
//application.getHandlers().addHandlers(mHandle);

        //goProgress(true ,mHandle);
       //application.goProgress(musicService!=null);

    }





    private String getTime(int dd){
        int minute=dd/60000;
        float se=((float)dd/60000)-minute;
        float second=se*60;
        return String.format("%02d:%02d",minute,(int)second);
    }

    /*@Override
    public void onEvent(int actionName) {
        switch (actionName){

            case MyApplication.MUSIC_LOADED:
                playing();
                startProgressUpdate();
                break;
            case MyApplication.MUSIC_LOADING:
                preparePlay();

                break;
            case MyApplication.MUSIC_SEEKCOMPLELED:
                playing();

                break;

        }
    }*/
    // 3.實作一個Delay執行緒


    // 調整播放進度
    private class MyRunControlOnSeekBarChangeListener
            implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int dest = seekBar.getProgress();  // 取得目前播放進度
           application.setSetProgress(true);
            application.setIsSeeking(true);
            music_loader.setVisibility(View.VISIBLE);

            //music_play.setVisibility(View.VISIBLE);
            //music_pause.setVisibility(View.INVISIBLE);
            musicFun.seekTo(dest);
        }
    }

    public void preparePlay(){
        music_loader.setVisibility(View.VISIBLE);
        //music_play.setVisibility(View.VISIBLE);
        //music_pause.setVisibility(View.INVISIBLE);
        music_title.setText(getMusicInfo().getTitle());
        mMax=getMusicInfo().getDuration();
        seekbar.setMax(mMax);
        String max=getTime(mMax);
        total_time.setText(max);

    }
    public void play(){
        if(musicFun!=null){
            musicFun.start();
        }else {
            playerbindHelper.bindService();
        }

    }
    public void pause(){
        if(playerbindHelper.isBound()){
            musicFun.pause();
        }else {
            playerbindHelper.bindService();
        }

    }
    //載入進度條進度的Handler
    /*private  Handler handler3=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //尚未到進度條進度
                case 0:

                    break;
                //已載入到進度條進度
                case 1:
                    playing();
                    break;
            }

            super.handleMessage(msg);
        }
    };
  // PreparedListener的Handler
private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        playing();
        startProgressUpdate();
        super.handleMessage(msg);
    }
};
    //Progress的Handler
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            position = msg.getData().getInt("progessValue");
            seekbar.setProgress(position);
            String now=getTime(position);
            run_time.setText(now);

        }
        /// }
    };
    // CompletionListener的Handler
    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            preparePlay();
             super.handleMessage(msg);
        }
    };*/
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.repeat:
                repeat.setVisibility(View.INVISIBLE);
                repeat_one.setVisibility(View.VISIBLE);
                application.setPlayMode(MyApplication.SINGLE_CYCLE);
                Toast.makeText(this,"單曲循環",Toast.LENGTH_SHORT).show();
                break;
            case R.id.shuffle:
                shuffle.setVisibility(View.INVISIBLE);
                repeat.setVisibility(View.VISIBLE);
                application.setPlayMode(MyApplication.ALL_CYCLE);
                Toast.makeText(this,"全部循環",Toast.LENGTH_SHORT).show();
                break;
            case R.id.repeat_one:
                repeat_one.setVisibility(View.INVISIBLE);
                shuffle.setVisibility(View.VISIBLE);
                application.setPlayMode(MyApplication.RANDOM_CYCLE);
                Toast.makeText(this,"隨機撥放",Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.music_play:

                play();
                break;
            case R.id.music_pause:
                pause();

                break;*/
            case R.id.music_next:
               musicFun.nextCircle();



                 break;
            case R.id.music_prev:
                musicFun.prevCircle();
                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("PlayActivity","onDestroy");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
       // setMusicBar();
    }

    private void FitViewById() {
        //music_play= (ImageButton) findViewById(R.id.music_play);
        //music_pause= (ImageButton) findViewById(R.id.music_pause);
        music_prev= (ImageButton) findViewById(R.id.music_prev);
        music_next= (ImageButton) findViewById(R.id.music_next);
        repeat= (ImageButton) findViewById(R.id.repeat);
        repeat_one= (ImageButton) findViewById(R.id.repeat_one);
        shuffle= (ImageButton) findViewById(R.id.shuffle);
        add_music_list= (ImageButton) findViewById(R.id.add_music_list);
        prev= (ImageButton) findViewById(R.id.prev);
        seekbar= (SeekBar) findViewById(R.id.seekBar);
        run_time= (TextView) findViewById(R.id.run_time);
        total_time= (TextView) findViewById(R.id.total_time);
        music_title= (TextView) findViewById(R.id.music_title);
        music_loader= (ProgressBar) findViewById(R.id.song_loader2);
        //music_play.setOnClickListener(this);
        //music_pause.setOnClickListener(this);
        music_prev.setOnClickListener(this);
        music_next.setOnClickListener(this);
        repeat_one.setOnClickListener(this);
        repeat.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        musicfun= (ImageView) findViewById(R.id.musicfun);
        playerImageView= (PlayerImageView) findViewById(R.id.player_btn);

    }
}
