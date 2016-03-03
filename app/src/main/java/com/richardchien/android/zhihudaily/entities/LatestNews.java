package com.richardchien.android.zhihudaily.entities;

import java.util.List;

/**
 * ZhihuDaily
 * Created by richard on 16/1/13.
 */
public class LatestNews {
    private String date;

    private List<Story> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
