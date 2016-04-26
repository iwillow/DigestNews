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

import rx.Observable;
import rx.Subscriber;


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

                content = extractURL(document);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (content != null && content.length() > 0) {
            return RequestAddress.NEWS_LIST_URL + content;
        }
        return content;
    }

    public static String extractURL(Document document) {
        String url = null;
        if (document != null) {
            Elements heads = document.getElementsByTag("head");
            if (heads != null && heads.size() > 0) {
                Element head = heads.first();
                Elements metes = head.getElementsByTag("meta");
                if (metes != null && metes.size() > 0) {
                    for (Element meta : metes) {
                        if (meta.hasAttr("property") && meta.hasAttr("content")
                                && meta.attr("property").equalsIgnoreCase("al:web:url")) {
                            url = meta.attr("content");
                            break;
                        }
                        if (meta.hasAttr("property") && meta.hasAttr("content")
                                && meta.attr("property").equalsIgnoreCase("og:url")) {
                            url = meta.attr("content");

                            break;
                        }
                    }
                }
            }
        }
        return url;
    }

    public static Observable<String> getContentUrl(final String url) {

        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                // TODO Auto-generated method stub
                try {
                    String content = parseContentURL(url);
                    if (content != null) {
                        subscriber.onNext(content);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Throwable("get content url failed."));
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static String parseContentURL(String url) throws IOException {
        String content = null;
        InputStream in = null;
        try {

            in = OkHttpSingleton.getInstance().getResponseInputStream(url);
            Document document = Jsoup.parse(in, "UTF-8", url);
            content = extractURL(document);

        } finally {
            if (in != null) {
                in.close();
            }
        }
        return content;
    }


}
