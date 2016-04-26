package com.iwillow.android.digestnews.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.iwillow.android.digestnews.api.RequestAddress;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.Items;
import com.iwillow.android.lib.log.LogUtil;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/4/26.
 */
public class RxNewsParser {

    public static String TAG = RxNewsParser.class.getSimpleName();

    public static Observable<List<Item>> getNewsList(String baseUrl, final String newsListUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            return null;
        }
        return Observable.just(baseUrl).map(new Func1<String, String>() {
            @Override
            public String call(String srcUrl) {

                String contentUrl = null;
                try {
                    contentUrl = NewsParser.parseContentURL(srcUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "" + e.getMessage());
                }
                return newsListUrl + contentUrl;

            }
        }).map(new Func1<String, String>() {

            @Override
            public String call(String url) {
                return get(url);
            }
        }).map(new Func1<String, List<Item>>() {
            @Override
            public List<Item> call(String json) {
                Gson gson = new Gson();
                Items item = gson.fromJson(json, Items.class);
                if (item != null && item.getItems() != null) {
                    return item.getItems();
                }
                return null;
            }
        });

    }

    public static Observable<List<Item>> getNewsContent(String uuid) {
        if (!TextUtils.isEmpty(uuid)) {
            return Observable.just(uuid).map(new Func1<String, String>() {
                @Override
                public String call(String s) {
                    String url = RequestAddress.NEWS_DETAIL_URL + s;
                    return get(url);
                }
            }).map(new Func1<String, List<Item>>() {
                @Override
                public List<Item> call(String json) {
                    Gson gson = new Gson();
                    Items item = gson.fromJson(json, Items.class);
                    if (item != null && item.getItems() != null) {
                        return item.getItems();
                    }
                    return null;
                }
            }).first();

        }
        return null;

    }

    public static String get(String url) {
        String content = null;
        try {
            content = OkHttpSingleton.getInstance().get(url);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.getMessage());
        }
        return content;
    }

}
