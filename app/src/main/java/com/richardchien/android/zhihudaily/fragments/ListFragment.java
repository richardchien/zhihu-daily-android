package com.richardchien.android.zhihudaily.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.richardchien.android.commonadapter.CommonAdapter;
import com.richardchien.android.commonadapter.ViewHolder;
import com.richardchien.android.zhihudaily.R;
import com.richardchien.android.zhihudaily.activities.StoryActivity;
import com.richardchien.android.zhihudaily.entities.LatestNews;
import com.richardchien.android.zhihudaily.entities.Story;
import com.richardchien.android.zhihudaily.others.Api;
import com.richardchien.android.zhihudaily.others.C;
import com.richardchien.android.zhihudaily.others.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * ZhihuDaily
 * Created by richard on 16/4/23.
 */
public class ListFragment extends BaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.list_view)
    ListView mListView;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String mStoriesJsonString;
    private List<Story> mStories = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView.setAdapter(new CommonAdapter<Story>(getContext(), mStories, R.layout.list_item) {
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyListViewAdapter();
    }

    public void refreshNews() {
        mSwipeRefreshLayout.setRefreshing(true);

        mHttpClient.cancelAllRequests(true);
        mHttpClient.get(getContext(), Api.LATEST_NEWS, new AsyncHttpResponseHandler() {
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
                Toast.makeText(ListFragment.this.getContext(), R.string.fail_to_refresh, Toast.LENGTH_SHORT).show();
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

    public void notifyListViewAdapter() {
        // Notify the adapter of mListView
        BaseAdapter adapter = (BaseAdapter) mListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getActivity().findViewById(R.id.fl_story_container) == null) {
            // Phone
            StoryActivity.start(getContext(), mStories.get(position).getId(), mStories.get(position).getTitle());
        } else {
            // Tablet
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_story_container, StoryFragment.newInstance(mStories.get(position).getId(), mStories.get(position).getTitle()))
                    .addToBackStack(null)
                    .commit();
        }

        mCache.put(C.CACHE_STORY_READ + mStories.get(position).getId(), C.CACHE_STORY_READ);
    }

    public void clearAllCache() {
        mCache.clear();
        clearCacheFolder(getContext().getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getContext().getCacheDir(), System.currentTimeMillis());
        clearCacheFolder(getContext().getExternalCacheDir(), System.currentTimeMillis());
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
