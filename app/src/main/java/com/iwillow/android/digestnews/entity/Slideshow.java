package com.iwillow.android.digestnews.entity;

import java.util.List;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class Slideshow {
    private int total;
    private String layout;
    private List<Photo> photos;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Slideshow{" +
                "total=" + total +
                ", layout='" + layout + '\'' +
                ", photos=" + photos +
                '}';
    }
}
