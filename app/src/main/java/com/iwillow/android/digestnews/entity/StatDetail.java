package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class StatDetail extends RealmObject{
    private String layout;
    private StatDetailMeta title;
    private StatDetailMeta value;
    private StatDetailMeta units;
    private StatDetailMeta description;
    private StatDetailMeta tragedy;

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public StatDetailMeta getTitle() {
        return title;
    }

    public void setTitle(StatDetailMeta title) {
        this.title = title;
    }

    public StatDetailMeta getValue() {
        return value;
    }

    public void setValue(StatDetailMeta value) {
        this.value = value;
    }

    public StatDetailMeta getUnits() {
        return units;
    }

    public void setUnits(StatDetailMeta units) {
        this.units = units;
    }

    public StatDetailMeta getDescription() {
        return description;
    }

    public void setDescription(StatDetailMeta description) {
        this.description = description;
    }

    public StatDetailMeta getTragedy() {
        return tragedy;
    }

    public void setTragedy(StatDetailMeta tragedy) {
        this.tragedy = tragedy;
    }

    @Override
    public String toString() {
        return "StatDetail{" +
                "layout='" + layout + '\'' +
                ", title=" + title +
                ", value=" + value +
                ", units=" + units +
                ", description=" + description +
                ", tragedy=" + tragedy +
                '}';
    }
}
