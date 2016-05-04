package com.iwillow.android.digestnews.entity;

import io.realm.RealmObject;

/**
 * Created by https://github.com/iwillow on 2016/4/27.
 */
public class StringRealmWrapper extends RealmObject {
    public String value;


    public String getValue() {
        return value;
    }

    public StringRealmWrapper setValue(String value) {
        this.value = value;
        return this;
    }
}
