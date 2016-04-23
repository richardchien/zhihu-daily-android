package com.richardchien.android.zhihudaily.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;

import org.afinal.simplecache.ACache;

/**
 * ZhihuDaily
 * Created by richard on 16/4/23.
 */
public class BaseFragment extends Fragment {
    protected ACache mCache;
    protected AsyncHttpClient mHttpClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCache = ACache.get(getContext().getApplicationContext());
        mHttpClient = new AsyncHttpClient();
    }
}
