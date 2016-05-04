package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class TweetMedia extends RealmObject{

    private String expanded_url;
    private String url;
    private String display_url;
    private String media_url;

    public String getExpanded_url() {
        return expanded_url;
    }

    public void setExpanded_url(String expanded_url) {
        this.expanded_url = expanded_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    @Override
    public String toString() {
        return "TweetMedia{" +
                "expanded_url='" + expanded_url + '\'' +
                ", url='" + url + '\'' +
                ", display_url='" + display_url + '\'' +
                ", media_url='" + media_url + '\'' +
                '}';
    }
}
