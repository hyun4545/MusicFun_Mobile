package com.example.myungjong.musicfun.Helper;

import android.app.Service;
import android.os.Binder;

/**
 * Created by myungjong on 2017/2/2.
 */

public abstract class MyBinder<S extends Service> extends Binder {
    public abstract S getService();
}
