package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class Source extends RealmObject{
    /***
     * data format
     * {
     *    "published": "2016-04-21T19:31:07Z",
     *    "title": "Prince dead: Live developments and reaction on the musicians passing",
     *    "publisher": "Yahoo News",
     *    "uuid": "",
     *    "url": "https://www.yahoo.com/news/princes-death--live-reaction-on-the-pop-stars-reported-passing-170445685.html?ref=gs"
     * }
     */
    private String published;
    private String title;
    private String publisher;
    private String uuid;
    private String url;

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Source{" +
                "published='" + published + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", uuid='" + uuid + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
