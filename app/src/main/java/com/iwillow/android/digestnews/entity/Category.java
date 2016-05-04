package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://github.com/iwillow on 2016/4/23.
 */
public class Category extends RealmObject{
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Category{" +
                "label='" + label + '\'' +
                '}';
    }
}
