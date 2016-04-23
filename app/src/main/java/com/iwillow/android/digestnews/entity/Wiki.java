package com.iwillow.android.digestnews.entity;

import java.util.List;

/**
 * Created by https://www.github/iwillow on 2016/4/23.
 */
public class Wiki {
    private String id;
    private String text;
    private String title;
    private Image images;
    private String url;
    private List<String> searchTerms;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(List<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    @Override
    public String toString() {
        return "Wiki{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", url='" + url + '\'' +
                ", searchTerms=" + searchTerms +
                '}';
    }
}
