package com.iwillow.android.digestnews.entity;

import java.util.List;

/**
 * Created by https://www/github.com/iwillow on 2016/4/23.
 */
public class TweetEntity {

    private List<TweetUrls> urls;

    private List<TweetMedia> media;

    private List<String> hashtags;

    private List<String> symbols;


    public List<TweetUrls> getUrls() {
        return urls;
    }

    public void setUrls(List<TweetUrls> urls) {
        this.urls = urls;
    }

    public List<TweetMedia> getMedia() {
        return media;
    }

    public void setMedia(List<TweetMedia> media) {
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
