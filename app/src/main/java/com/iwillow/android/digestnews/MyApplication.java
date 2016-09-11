package com.iwillow.android.digestnews;

import android.app.Application;
import android.os.RemoteException;

import com.amap.api.maps2d.MapsInitializer;
import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by https://www.github.com/iwillow on 2016/4/27.
 */
public class MyApplication extends Application {
    public static MyApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this).name("newsdigest.realm").build();
        Realm.setDefaultConfiguration(config);
        LeakCanary.install(this);
        INSTANCE = this;
        try {
            MapsInitializer.initialize(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static MyApplication getInstance() {
        return INSTANCE;
    }
}
