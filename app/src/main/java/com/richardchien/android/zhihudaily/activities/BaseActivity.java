package com.richardchien.android.zhihudaily.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;

import org.afinal.simplecache.ACache;

/**
 * ZhihuDaily
 * Created by richard on 16/1/13.
 */
public class BaseActivity extends AppCompatActivity {
    protected ACache mCache;
    protected AsyncHttpClient mHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCache = ACache.get(getApplicationContext());
        mHttpClient = new AsyncHttpClient();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
