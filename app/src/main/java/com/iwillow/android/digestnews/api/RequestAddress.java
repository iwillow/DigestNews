package com.iwillow.android.digestnews.api;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class RequestAddress {

    /**
     * yahoo news digest api url
     */
    public static final String BASE_URL = "https://www.yahoo.com/digest";
    /**
     * query news list according to news's url
     */
    public static final String NEWS_LIST_URL = "https://www.yahoo.com/digest/_td_api/api/resource/digest;url=";
    /**
     * query  detail news according to news's uuid
     */
    public static final String NEWS_DETAIL_URL = "https://www.yahoo.com/digest/_td_api/api/resource/digest;uuids=";
}
