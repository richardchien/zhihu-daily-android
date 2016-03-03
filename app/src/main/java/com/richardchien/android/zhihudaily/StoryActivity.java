package com.richardchien.android.zhihudaily;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.richardchien.android.zhihudaily.entities.StoryDetails;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class StoryActivity extends MyActivity {
    @Bind(R.id.web_view)
    WebView mWebView;

    @Bind(R.id.text_title)
    TextView mTextTitle;

    @Bind(R.id.text_source)
    TextView mTextImageSource;

    @Bind(R.id.image_head)
    SimpleDraweeView mDraweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt(C.KEY_ID);
        String title = bundle.getString(C.KEY_TITLE);

        mTextTitle.setText(title);
        loadContent(id);
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
}
