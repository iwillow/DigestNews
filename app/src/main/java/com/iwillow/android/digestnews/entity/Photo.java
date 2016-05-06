package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class Photo extends RealmObject{
    private String headline;
    private String provider_name;
    private String caption;
    private Image images;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "headline='" + headline + '\'' +
                ", provider_name='" + provider_name + '\'' +
                ", caption='" + caption + '\'' +
                ", images=" + images +
                '}';
    }
}
