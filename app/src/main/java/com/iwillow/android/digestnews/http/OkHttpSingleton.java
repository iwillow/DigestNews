package com.iwillow.android.digestnews.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by https://www.github.com/iwillow on 2016/4/26.
 */
public class OkHttpSingleton {

    public volatile static OkHttpSingleton instance;

    private OkHttpClient mOkHttpClient;


    private OkHttpSingleton() {
        mOkHttpClient = getOkHttpClient();
    }

    public static OkHttpSingleton getInstance() {
        if (instance == null) {
            synchronized (OkHttpSingleton.class) {
                if (instance == null) {
                    instance = new OkHttpSingleton();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        return mOkHttpClient;
    }


    public String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = getOkHttpClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public InputStream getResponseInputStream(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = getOkHttpClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().byteStream();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String post(String url, Map<String, String> parms) throws IOException {

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (parms != null && parms.keySet().size() > 0) {
            Set<Map.Entry<String, String>> keySet = parms.entrySet();
            for (Map.Entry<String, String> kvEntry : keySet) {
                formBodyBuilder.add(kvEntry.getKey(), kvEntry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        Response response = getOkHttpClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public InputStream postResponseInputStream(String url, Map<String, String> parms) throws IOException {

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (parms != null && parms.keySet().size() > 0) {
            Set<Map.Entry<String, String>> keySet = parms.entrySet();
            for (Map.Entry<String, String> kvEntry : keySet) {
                formBodyBuilder.add(kvEntry.getKey(), kvEntry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        Response response = getOkHttpClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().byteStream();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
