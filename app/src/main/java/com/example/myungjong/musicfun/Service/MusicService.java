package com.example.myungjong.musicfun.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Helper.MusicFun;
import com.example.myungjong.musicfun.Helper.MusicInfo;
import com.example.myungjong.musicfun.Helper.MyBinder;
import com.example.myungjong.musicfun.Helper.RegisterEvent;
import com.example.myungjong.musicfun.Model.MusicHandlers;
import com.example.myungjong.musicfun.Model.MyHandler;
import com.example.myungjong.musicfun.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener {
    private final MyBinder<MusicService> binder = new ServiceBinder();
    private MediaPlayer mediaPlayer;
    NotificationManager notificationManager;
    Notification.Builder builder;
    RemoteViews remoteViews;
    Notification notification;
    Boolean p_btnVisiable=false;
    Handler handler=new Handler();
    Map<Integer,Handler> handlerMap=new HashMap<Integer, Handler>();
    Map<Integer,Handler> handlerMap2=new HashMap<Integer, Handler>();
    MusicHandlers handlers;
    MusicHandlers handlers2;
    Thread playThread;

    boolean isPrepared=true;
    private int id=-1;
    int Duration=3;
    int uploadProgress;
    MyApplication application;
    int progress;
    Notification.Builder mBuilder;
    public MusicFun musicFun;




    @Override
    public void onCreate() {
        application= (MyApplication) getApplicationContext();
        musicFun=new MusicFun(application.getToken(),this);
        setRemoteViews();
        musicFun.addMusicFunListener(new MusicFun.MusicFunListener() {
            @Override
            public void onProgressUpdate(int progress) {

            }

            @Override
            public void onSeekComplete() {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onPrepare() {
                if(notificationManager==null){
                    createNotification();
                }
                    remoteViews.setTextViewText(R.id.notification_singname,getMusicInfo().getTitle());
                    remoteViews.setTextViewText(R.id.notification_author,getMusicInfo().getAuthor());
                    remoteViews.setViewVisibility(R.id.notification_loader,View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.notification_play, View.INVISIBLE);
                    remoteViews.setViewVisibility(R.id.notification_pause, View.VISIBLE);
                    notificationManager.notify(44,notification);

            }

            @Override
            public void onPrepared() {
                remoteViews.setViewVisibility(R.id.notification_loader,View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.notification_pause, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.notification_play, View.VISIBLE);
                notificationManager.notify(44,notification);
            }

            @Override
            public void onBuffering() {
                remoteViews.setViewVisibility(R.id.notification_loader,View.VISIBLE);
                remoteViews.setViewVisibility(R.id.notification_play, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.notification_pause, View.VISIBLE);
                notificationManager.notify(44,notification);
            }

            @Override
            public void onPause() {
                remoteViews.setViewVisibility(R.id.notification_play, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.notification_pause, View.VISIBLE);
                notificationManager.notify(44,notification);
            }

            @Override
            public void onStart() {
                remoteViews.setViewVisibility(R.id.notification_pause, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.notification_play, View.VISIBLE);
                notificationManager.notify(44,notification);
            }
        });

        super.onCreate();





    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.w("onBind","onbind is call");
        //Toast.makeText(this, "onBind", Toast.LENGTH_SHORT).show();

        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.w("Rebind","Rebind is call");
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()){
            case "MusicPlay":
                musicFun.start();


                break;
            case "MusicPause":
                musicFun.pause();


                break;
            case "MusicNext":
                musicFun.nextCircle();
                break;
            case "Cancel":
                Log.e("cancel","true");
                notificationManager.cancel(44);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Duration=mp.getDuration();
    }

    public class ServiceBinder extends MyBinder<MusicService> {

        @Override
        public MusicService getService() {
            return MusicService.this;
        }
    }



    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private PendingIntent IntentAction(String action_name){
        Intent switchIntent=new Intent(this,MusicService.class);
        switchIntent.setAction(action_name);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,switchIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }
    public boolean isNotification(){
        return notification!=null;
    }

    public void setRemoteViews(){
        remoteViews=new RemoteViews(this.getPackageName(), R.layout.notification_music_player);
        remoteViews.setOnClickPendingIntent(R.id.notification_pause, IntentAction("MusicPlay"));
        remoteViews.setOnClickPendingIntent(R.id.notification_play, IntentAction("MusicPause"));
        remoteViews.setOnClickPendingIntent(R.id.imageButton6,IntentAction("MusicNext"));
        remoteViews.setOnClickPendingIntent(R.id.notification_cancel,IntentAction("Cancel"));
    }
    public void createNotification(){

        notificationManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
       notificationManager.cancelAll();





        /*Intent switchIntent=new Intent("MusicPlay");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,switchIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        Intent switchIntent2=new Intent("MusicPause");
        PendingIntent pendingIntent2=PendingIntent.getBroadcast(this,1,switchIntent2,PendingIntent.FLAG_CANCEL_CURRENT);*/
/*if (true){
    remoteViews.setViewVisibility(R.id.notification_play, View.INVISIBLE);
    remoteViews.setViewVisibility(R.id.notification_pause, View.VISIBLE);
}else{
    remoteViews.setViewVisibility(R.id.notification_pause, View.INVISIBLE);
    remoteViews.setViewVisibility(R.id.notification_play, View.VISIBLE);
}*/
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

       // remoteViews.setViewVisibility(R.id.btn_play, View.INVISIBLE);
        mBuilder = new Notification.Builder(this)

                .setSmallIcon(R.mipmap.logo_white_ver31)
                //.setCustomBigContentView()
                .setOngoing(true)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_MAX);
        notification = mBuilder
                .build();

        Log.w("version", Integer.toString(Build.VERSION.SDK_INT));
        //notification.contentView=remoteViews;
        notification.bigContentView=remoteViews;
        notification.contentView=remoteViews;

       //startForeground(100,notification);
       notificationManager.notify(44,notification);
        /*handlers2.addHandlers(notificationHandler1);
        handlers.addHandlers(notificationHandler2);
        application.getHandlers2().addHandlers(notificationHandler3);*/

    }
private MusicInfo getMusicInfo(){
   return MusicInfo.getInstance();
}
private Handler notificationHandler1=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        remoteViews.setTextViewText(R.id.notification_singname,getMusicInfo().getTitle());
        remoteViews.setTextViewText(R.id.notification_author,getMusicInfo().getMusicLists().get(getMusicInfo().getPosition()).getAuthor());
        remoteViews.setViewVisibility(R.id.notification_play, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.notification_pause, View.INVISIBLE);
        remoteViews.setViewVisibility(R.id.notification_loader,View.VISIBLE);
        notificationManager.notify(44,notification);
        super.handleMessage(msg);
    }
};
private void setRemotePlayer(){
    if(true){
        remoteViews.setViewVisibility(R.id.notification_play, View.INVISIBLE);
        remoteViews.setViewVisibility(R.id.notification_pause, View.VISIBLE);
    }else {
        remoteViews.setViewVisibility(R.id.notification_play, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.notification_pause, View.INVISIBLE);
    }
    notificationManager.notify(44,notification);
}

    private Handler notificationHandler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            remoteViews.setViewVisibility(R.id.notification_play, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.notification_pause, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.notification_loader,View.INVISIBLE);
            notificationManager.notify(44,notification);
            //super.handleMessage(msg);
        }
    };

    //載入進度條進度的Handler
    private  Handler notificationHandler3=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //尚未到進度條進度
                case 0:
                    remoteViews.setViewVisibility(R.id.notification_play, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.notification_pause, View.INVISIBLE);
                    remoteViews.setViewVisibility(R.id.notification_loader,View.VISIBLE);
                    notificationManager.notify(44,notification);
                    break;
                //已載入到進度條進度
                case 1:
                    remoteViews.setViewVisibility(R.id.notification_play, View.INVISIBLE);
                    remoteViews.setViewVisibility(R.id.notification_pause, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.notification_loader,View.INVISIBLE);
                    notificationManager.notify(44,notification);
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
    @Override
    public void onDestroy() {
        notificationManager.cancel(44);
        super.onDestroy();
    }
}
