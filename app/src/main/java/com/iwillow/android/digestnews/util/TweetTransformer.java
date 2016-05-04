package com.iwillow.android.digestnews.util;

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
                        sb.append("<a href=\"").append(tag).append(array[i]).append("\">").append(array[i]).append("</a>");

                    } else if (array[i].startsWith("@")) {
                        sb.append("<a href=\"").append(url).append(array[i].substring(1)).append("\">").append(array[i])
                                .append("</a>");

                    } else if (array[i].startsWith("http")) {

                        sb.append("<a href=\"").append(array[i]).append("\">").append(array[i]).append("</a>");

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


}
