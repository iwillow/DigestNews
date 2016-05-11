package com.iwillow.android.digestnews.util;

import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.entity.Source;

import io.realm.RealmList;

/**
 * Created by https://www.github.com/iwillow on 2016/5/11.
 */
public class ItemRealUtil {


    public static String getPress(ItemRealm itemRealm) {
        StringBuilder sb = new StringBuilder();
        RealmList<Source> presses = itemRealm.getSources();

        if (presses != null && presses.size() > 0) {


            if (presses.size() == 1) {
                sb.append(presses.get(0).getPublisher());
            } else if (presses.size() == 2) {
                sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
            } else if (presses.size() == 3) {
                sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
                sb.append(" + 1 more");
            } else if (presses.size() == 4) {
                sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
                sb.append(" + 2 more");
            } else {
                sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
                sb.append(" + 3 more");
            }
        } else {
            sb.append("Yahoo News");
        }
        return sb.toString();
    }

}
