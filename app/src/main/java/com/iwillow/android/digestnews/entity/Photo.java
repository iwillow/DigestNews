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
