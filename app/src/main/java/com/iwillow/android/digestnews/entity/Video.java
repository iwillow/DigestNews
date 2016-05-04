package com.iwillow.android.digestnews.entity;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class Video extends RealmObject {
    private String thumbnail;
    private String title;
    private String uuid;
    private String summary;
    private String publisher;
    private RealmList<Stream> streams;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public RealmList<Stream> getStreams() {
        return streams;
    }

    public void setStreams(RealmList<Stream> streams) {
        this.streams = streams;
    }

    @Override
    public String toString() {
        return "Video{" +
                "thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", uuid='" + uuid + '\'' +
                ", summary='" + summary + '\'' +
                ", publisher='" + publisher + '\'' +
                ", streams=" + streams +
                '}';
    }
}
