package com.iwillow.android.digestnews;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by https://www.github.com/iwillow on 2016/4/27.
 */
public class MyApplicaition extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this).name("newsdigest.realm").build();
        Realm.setDefaultConfiguration(config);
        LeakCanary.install(this);

    }

}
