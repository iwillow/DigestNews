package com.iwillow.android.digestnews.entity;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by https://www/github.com/iwillow on 2016/4/23.
 */
public class TweetEntity {

    private RealmList<TweetUrls> urls;

    private RealmList<TweetMedia> media;

    private List<String> hashtags;

    private List<String> symbols;


    public RealmList<TweetUrls> getUrls() {
        return urls;
    }

    public void setUrls(RealmList<TweetUrls> urls) {
        this.urls = urls;
    }

    public RealmList<TweetMedia> getMedia() {
        return media;
    }

    public void setMedia(RealmList<TweetMedia> media) {
        this.media = media;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        return "TweetEntity{" +
                "urls=" + urls +
                ", media=" + media +
                ", hashtags=" + hashtags +
                ", symbols=" + symbols +
                '}';
    }
}
