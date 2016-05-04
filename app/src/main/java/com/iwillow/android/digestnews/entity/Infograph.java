package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://github.com/iwillow on 2016/4/23.
 */
public class Infograph extends RealmObject {
    private String type;
    private String title;
    private String caption;
    private Image images;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "Infograph{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", caption='" + caption + '\'' +
                ", images=" + images +
                '}';
    }
}
