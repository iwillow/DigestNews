package com.iwillow.android.digestnews.http;

import com.iwillow.android.digestnews.api.RequestAddress;
import com.iwillow.android.lib.log.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class NewsParser {


    public static String getNewsListUrl() {
        String content = null;
        try {
            URL url = new URL(RequestAddress.BASE_URL);
            Document document = Jsoup.parse(url, 10000);
            //Document document = Jsoup.connect(RequestAddress.BASE_URL).timeout(10000).execute().parse();
            if (document != null) {

                Elements heads = document.getElementsByTag("head");
                if (heads != null && heads.size() > 0) {
                    Element head = heads.first();
                    Elements metes = head.getElementsByTag("meta");
                    if (metes != null && metes.size() > 0) {
                        LogUtil.d("NewsParser", "metes:\n" + metes.size());
                        for (Element meta : metes) {
                            if (meta.hasAttr("property") && meta.hasAttr("content")
                                    && meta.attr("property").equalsIgnoreCase("al:web:url")) {
                                content = meta.attr("content");
                                break;
                            }
                            if (meta.hasAttr("property") && meta.hasAttr("content")
                                    && meta.attr("property").equalsIgnoreCase("og:url")) {
                                content = meta.attr("content");

                                break;
                            }
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null && content.length() > 0) {
            return RequestAddress.NEWS_LIST_URL + content;
        }
        return null;
    }

    /**
     * extract the news list url from document InputStream
     *
     * @param inputStream Document InputStream
     * @param url         document's original url
     * @return news list url
     */
    public static String getNewsListUrl(InputStream inputStream, String url) {
        String content = null;
        if (inputStream != null) {
            try {
                Document document = Jsoup.parse(inputStream, "UTF-8", url);
                if (document != null) {
                    Elements heads = document.getElementsByTag("head");
                    if (heads != null && heads.size() > 0) {
                        Element head = heads.first();
                        Elements metes = head.getElementsByTag("meta");
                        if (metes != null && metes.size() > 0) {
                            for (Element meta : metes) {
                                if (meta.hasAttr("property") && meta.hasAttr("content")
                                        && meta.attr("property").equalsIgnoreCase("al:web:url")) {
                                    content = meta.attr("content");
                                    break;
                                }
                                if (meta.hasAttr("property") && meta.hasAttr("content")
                                        && meta.attr("property").equalsIgnoreCase("og:url")) {
                                    content = meta.attr("content");

                                    break;
                                }
                            }
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (content != null && content.length() > 0) {
            return RequestAddress.NEWS_LIST_URL + content;
        }
        return content;
    }

}
