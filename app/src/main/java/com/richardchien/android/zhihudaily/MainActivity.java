package com.richardchien.android.zhihudaily;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.richardchien.android.commonadapter.CommonAdapter;
import com.richardchien.android.commonadapter.ViewHolder;
import com.richardchien.android.zhihudaily.entities.LatestNews;
import com.richardchien.android.zhihudaily.entities.Story;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends MyActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.list_view)
    ListView mListView;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String mStoriesJsonString;
    private List<Story> mStories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView.setAdapter(new CommonAdapter<Story>(this, mStories, R.layout.list_item) {
            @Override
            public void onPostCreateView(ViewHolder holder, Story story) {
                SimpleDraweeView draweeView = holder.getView(R.id.image_story_small);
                draweeView.setImageURI(Uri.parse(story.getImageUrls().get(0)));

                holder.setViewText(R.id.text_title, story.getTitle());
                holder.setViewVisibility(R.id.text_many_pics, story.hasManyPics() ? View.VISIBLE : View.INVISIBLE);

                String read = mCache.getAsString(C.CACHE_STORY_READ + story.getId());
                if (read != null && read.equals(C.CACHE_STORY_READ)) {
                    holder.setViewTextColor(R.id.text_title, Color.parseColor("#666666"));
                } else {
                    holder.setViewTextColor(R.id.text_title, Color.parseColor("#222222"));
                }
            }
        });

        mListView.setOnItemClickListener(this);

        mStoriesJsonString = mCache.getAsString(C.CACHE_LATEST_NEWS);
        if (mStoriesJsonString != null) {
            updateNewsList();
        }

        refreshNews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyListViewAdapter();
    }

    private void refreshNews() {
        mSwipeRefreshLayout.setRefreshing(true);

        mHttpClient.cancelAllRequests(true);
        mHttpClient.get(this, Api.LATEST_NEWS, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mSwipeRefreshLayout.setRefreshing(false);

                String jsonString = Helper.stringFromBytes(responseBody);
                if (jsonString != null) {
                    mStoriesJsonString = jsonString;
                    updateNewsList();
                    mCache.put(C.CACHE_LATEST_NEWS, jsonString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Info", "Fail to fetch latest news");
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, R.string.fail_to_refresh, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNewsList() {
        // Parse json string to get news object
        LatestNews news = JSON.parseObject(mStoriesJsonString, LatestNews.class);
        List<Story> tmpList = news.getStories();
        mStories.clear();
        for (Story story : tmpList) {
            mStories.add(story);
        }
        notifyListViewAdapter();
    }

    private void notifyListViewAdapter() {
        // Notify the adapter of mListView
        BaseAdapter adapter = (BaseAdapter) mListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(C.KEY_ID, mStories.get(position).getId());
        bundle.putString(C.KEY_TITLE, mStories.get(position).getTitle());
        intent.putExtras(bundle);
        startActivity(intent);

        mCache.put(C.CACHE_STORY_READ + mStories.get(position).getId(), C.CACHE_STORY_READ);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshNews();
                break;
            case R.id.action_clear_cache:
                clearAllCache();
                notifyListViewAdapter();
        }
        return true;
    }

    private void clearAllCache() {
        mCache.clear();
        clearCacheFolder(getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getCacheDir(), System.currentTimeMillis());
        clearCacheFolder(getExternalCacheDir(), System.currentTimeMillis());
        mCache.put(C.CACHE_LATEST_NEWS, mStoriesJsonString);
    }

    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    @Override
    public void onRefresh() {
        refreshNews();
    }
}
