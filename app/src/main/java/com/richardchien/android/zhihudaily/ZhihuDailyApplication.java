package com.richardchien.android.zhihudaily;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * ZhihuDaily
 * Created by richard on 16/1/13.
 */
public class ZhihuDailyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
