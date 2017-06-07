package com.example.myungjong.musicfun.Helper;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.myungjong.musicfun.Model.Callback;
import com.example.myungjong.musicfun.Service.MusicService;

/**
 * Created by myungjong on 2017/1/29.
 */

public class BindHelper<T extends Service,B extends MyBinder<T>>  {
    private Context ctx;
    private boolean isBound;
    private T service;
    Class<T> service_class;
    boolean rr;
    Callback<T> callback;
    public BindHelper(Context context, Class<T> service_class,Callback<T> callback){
        this.ctx=context;
        isBound=false;
        this.service_class=service_class;
        this.callback=callback;
        //bindService();
    }

    public boolean isBound() {
        return isBound;
    }

    public void bindService(){
        //Log.e(service_class.getName()+" isbound", Boolean.toString(isBound));
        if(!isBound){
            Intent intent=new Intent(ctx,service_class);
            rr = ctx.getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            isBound=true;
        }
    }
    public void unBindService(){
        if(isBound){
            ctx.unbindService(serviceConnection);
            isBound=false;
        }
    }

    public boolean isRr() {
        return rr;
    }

    public T getService(){
        Log.e("service3",Boolean.toString(service==null));
        return service;
    }
   private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service= ((B) iBinder).getService();
            callback.doSomething(service);
            Log.e("service2",Boolean.toString(service==null));
            Log.e("onServiceConnected","fsf");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service=null;


        }
    };


}
