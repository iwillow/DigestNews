package com.iwillow.android.digestnews.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by https://www.github.com/iwillow on 2016/5/4.
 */
public class TweetTransformer {


    public static String convert(String sourceTweet) {
        StringBuilder sb = new StringBuilder();

        if (sourceTweet != null && sourceTweet.length() > 0) {
            String url = "https://twitter.com/";
            String tag = "https://mobile.twitter.com/search";
            String[] array = sourceTweet.split(" ");
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    if (array[i].startsWith("#")) {
                        sb.append("<a  style=\"text-decoration:none;\"   href=\"").append(tag).append(array[i]).append("\">").append(array[i]).append("</a>");

                    } else if (array[i].startsWith("@")) {
                        sb.append("<a  style=\"text-decoration:none;\"  href=\"").append(url).append(array[i].substring(1)).append("\">").append(array[i])
                                .append("</a>");

                    } else if (array[i].startsWith("http")) {

                        sb.append("<a  style=\"text-decoration:none;\"  href=\"").append(array[i]).append("\">").append(array[i]).append("</a>");

                    } else {
                        sb.append(array[i]);
                    }
                    if (!array[i].endsWith(";") && !array[i].endsWith(",")) {
                        sb.append(" ");
                    }
                }
            }
        }
   
        return sb.toString();
    }
    public static String twitterTime(String createdTime) {
        String result = "1d";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try {
            Date date = sdf.parse(createdTime);
            long t2 = date.getTime();
            long t1 = System.currentTimeMillis();
            Long delta = Long.valueOf(TimeUnit.MILLISECONDS.toHours(Long.valueOf(t1 - t2).longValue()));
            if (delta.longValue() < 1L) {
                return "1h";
            }
            if (delta.longValue() < 24L) {
                return delta.longValue() + "h";
            }
            return delta.longValue() / 24 + "d";

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return result;
    }

}
