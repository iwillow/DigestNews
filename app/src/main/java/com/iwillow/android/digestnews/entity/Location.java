package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://github.com/iwillow on 2016/4/23.
 */
public class Location extends RealmObject{
    private String latitude;
    private String longtitude;
    private String zoonLevel;
    private String type;
    private String caption;
    private String name;

    public String getLatitude() {
        return latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getZoonLevel() {
        return zoonLevel;
    }

    public String getType() {
        return type;
    }

    public String getCaption() {
        return caption;
    }

    public String getName() {
        return name;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public void setZoonLevel(String zoonLevel) {
        this.zoonLevel = zoonLevel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", zoonLevel='" + zoonLevel + '\'' +
                ", type='" + type + '\'' +
                ", caption='" + caption + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
