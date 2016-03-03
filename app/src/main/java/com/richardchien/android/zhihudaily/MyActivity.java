package com.richardchien.android.zhihudaily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;

import org.afinal.simplecache.ACache;

/**
 * ZhihuDaily
 * Created by richard on 16/1/13.
 */
public class MyActivity extends AppCompatActivity {
    protected ACache mCache;
    protected AsyncHttpClient mHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCache = ACache.get(this);
        mHttpClient = new AsyncHttpClient();
    }
}
