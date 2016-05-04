package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by  https://github.com/iwillow on 2016/4/23.
 */
public class Quote extends RealmObject {
    private String url;
    private String text;
    private String source;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "url='" + url + '\'' +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
