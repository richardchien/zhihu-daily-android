package com.richardchien.android.zhihudaily.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.richardchien.android.zhihudaily.R;
import com.richardchien.android.zhihudaily.entities.StoryDetails;
import com.richardchien.android.zhihudaily.others.Api;
import com.richardchien.android.zhihudaily.others.C;
import com.richardchien.android.zhihudaily.others.Helper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * ZhihuDaily
 * Created by richard on 16/4/23.
 */
public class StoryFragment extends BaseFragment {
    @Bind(R.id.web_view)
    WebView mWebView;

    @Bind(R.id.text_title)
    TextView mTextTitle;

    @Bind(R.id.text_source)
    TextView mTextImageSource;

    @Bind(R.id.image_head)
    SimpleDraweeView mDraweeView;

    private int mId;
    private String mTitle;

    public static StoryFragment newInstance(int id, String title) {
        Bundle args = new Bundle();
        args.putInt(C.KEY_ID, id);
        args.putString(C.KEY_TITLE, title);
        StoryFragment fragment = new StoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        ButterKnife.bind(this, view);
        mId = getArguments().getInt(C.KEY_ID);
        mTitle = getArguments().getString(C.KEY_TITLE);
        load();
        return view;
    }

    public void load() {
        mTextTitle.setText(mTitle);
        loadContent(mId);
    }

    private void loadContent(int id) {
        final String cacheKey = C.CACHE_STORY_DETAIL + id;

        String jsonString = mCache.getAsString(cacheKey);
        if (jsonString != null) {
            displayContent(jsonString);
        } else {
            mHttpClient.get(String.format(Api.NEWS, id), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String jsonString = Helper.stringFromBytes(responseBody);
                    if (jsonString != null) {
                        displayContent(jsonString);
                        mCache.put(cacheKey, jsonString);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("Info", "Fail to load story details");
                }
            });
        }
    }

    private void displayContent(String jsonString) {
        StoryDetails details = JSON.parseObject(jsonString, StoryDetails.class);
        mDraweeView.setImageURI(Uri.parse(details.getImageUrl()));
        mTextImageSource.setText("图片：" + details.getImageSource());
        mWebView.loadData(buildHtml(details.getHtmlBody(), details.getCssUrls(), details.getJsUrls()), "text/html; charset=UTF-8", null);
    }

    private String buildHtml(String htmlBody, List<String> cssUrls, List<String> jsUrls) {
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append("<head>\n");
        for (String cssUrl : cssUrls) {
            headBuilder.append("<link rel='stylesheet' type='text/css' href='").append(cssUrl).append("'>\n");
        }
        for (String jsUrl : jsUrls) {
            headBuilder.append("<script src='").append(jsUrl).append("'></script>\n");
        }
        headBuilder.append("</head>");
        String head = headBuilder.toString();
        String body = "<body>\n" + htmlBody + "\n</body>";
        String html =
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        head + "\n" +
                        body + "\n" +
                        "</html>";
        return html;
    }

    public void openInBrowser() {
        Intent view = new Intent();
        view.setAction(Intent.ACTION_VIEW);
        view.setData(Uri.parse(String.format(Api.SHARE_URL, mId)));
        startActivity(view);
    }
}
