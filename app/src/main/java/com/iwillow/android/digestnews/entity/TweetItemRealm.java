package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2016/4/27.
 */
public class TweetItemRealm extends RealmObject {

    private String id;
    private String text;
    private String possibly_sensitive;
    private String created_at;
    private TweetUser user;
    private TweetEntityRealm entities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPossibly_sensitive() {
        return possibly_sensitive;
    }

    public void setPossibly_sensitive(String possibly_sensitive) {
        this.possibly_sensitive = possibly_sensitive;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public TweetUser getUser() {
        return user;
    }

    public void setUser(TweetUser user) {
        this.user = user;
    }

    public TweetEntityRealm getEntities() {
        return entities;
    }

    public void setEntities(TweetEntityRealm entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "TweetItemRealm{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", possibly_sensitive='" + possibly_sensitive + '\'' +
                ", created_at='" + created_at + '\'' +
                ", user=" + user +
                ", entities=" + entities +
                '}';
    }
}
