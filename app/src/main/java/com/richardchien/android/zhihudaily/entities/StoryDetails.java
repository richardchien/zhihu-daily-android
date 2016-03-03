package com.richardchien.android.zhihudaily.entities;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * ZhihuDaily
 * Created by richard on 16/1/13.
 */
public class StoryDetails {
    private int id;

    private String title;

    @JSONField(name = "image")
    private String imageUrl;

    @JSONField(name = "image_source")
    private String imageSource;

    @JSONField(name = "body")
    private String htmlBody;

    @JSONField(name = "js")
    private List<String> jsUrls;

    @JSONField(name = "css")
    private List<String> cssUrls;

    @JSONField(name = "share_url")
    private String shareUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public List<String> getJsUrls() {
        return jsUrls;
    }

    public void setJsUrls(List<String> jsUrls) {
        this.jsUrls = jsUrls;
    }

    public List<String> getCssUrls() {
        return cssUrls;
    }

    public void setCssUrls(List<String> cssUrls) {
        this.cssUrls = cssUrls;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
