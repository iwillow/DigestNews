package com.iwillow.android.digestnews.entity;

import java.util.List;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class Tweet {
    private String uuid;
    private String item_id;
    private List<TweetItem> tweets;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public List<TweetItem> getTweets() {
        return tweets;
    }

    public void setTweets(List<TweetItem> tweets) {
        this.tweets = tweets;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "uuid='" + uuid + '\'' +
                ", item_id='" + item_id + '\'' +
                ", tweets=" + tweets +
                '}';
    }
}
