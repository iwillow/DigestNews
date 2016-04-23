package com.iwillow.android.digestnews.http;

import com.android.volley.Response;

/**
 * to reduce the quantities of interfaces, create an interface which implements com.android.volley.Response.ErrorListener and com.android.volley.Response.Listener
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public interface ResponseListener<T> extends Response.ErrorListener,
        Response.Listener<T> {
}
