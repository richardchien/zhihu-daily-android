package com.richardchien.android.zhihudaily.entities;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * ZhihuDaily
 * Created by richard on 16/1/13.
 */
public class Story {
    private int id;

    @JSONField(name = "images")
    private List<String> imageUrls;

    private String title;

    @JSONField(name = "multipic")
    private boolean hasManyPics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasManyPics() {
        return hasManyPics;
    }

    public void setHasManyPics(boolean hasManyPics) {
        this.hasManyPics = hasManyPics;
    }
}
