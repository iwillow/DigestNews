package com.iwillow.android.digestnews.db;

import android.text.TextUtils;

import com.iwillow.android.digestnews.entity.ItemRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by https://www.github.com/iwillow on 2016/4/27.
 */
public class NewsDAOImp implements NewsDAO {

    @Override
    public ItemRealm addNews(ItemRealm itemRealm, Realm realm) {
        ItemRealm finalItem = null;
        if (realm != null && itemRealm != null) {
            try {
                realm.beginTransaction();
                finalItem = realm.copyToRealm(itemRealm);
                realm.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                finalItem = null;
            }
        }

        return finalItem;
    }

    @Override
    public boolean deleteNews(String id, Realm realm) {
        boolean success = false;
        if (realm != null) {
            if (id != null && "".equals(id)) {
                RealmResults<ItemRealm> items = realm.where(ItemRealm.class).findAll();
                realm.beginTransaction();
                success = items.deleteAllFromRealm();
                realm.commitTransaction();
            } else if ((id != null && id.length() > 0)) {
                RealmResults<ItemRealm> items = realm.where(ItemRealm.class).equalTo("id", id).findAll();
                realm.beginTransaction();
                success = items.deleteAllFromRealm();
                realm.commitTransaction();
            }
        }
        return success;
    }

    @Override
    public List<ItemRealm> queryNews(String id, Realm realm) {

        if (realm != null) {
            RealmResults<ItemRealm> sets;
            if (TextUtils.isEmpty(id)) {
                sets = realm.where(ItemRealm.class).findAll().sort("id");

            } else {
                sets = realm.where(ItemRealm.class).equalTo("id", id).findAll();
            }
            List<ItemRealm> list = new ArrayList<>();
            if (sets != null) {
                for (ItemRealm itemRealm : sets) {
                    list.add(itemRealm);
                }
            }
            return list;
        }

        return null;
    }

    @Override
    public boolean updateNews(ItemRealm itemRealm, Realm realm) {
        boolean success = false;
        if (realm != null && itemRealm != null) {
            try {
                ItemRealm toBeEdit = realm.where(ItemRealm.class).equalTo("id", itemRealm.getId()).findAll().first();
                realm.beginTransaction();
                toBeEdit.setColors(itemRealm.getColors());
                toBeEdit.setCategories(itemRealm.getCategories());
                toBeEdit.setCluster_id(itemRealm.getCluster_id());
                toBeEdit.setDeeplinkIOS(itemRealm.getDeeplinkIOS());
                toBeEdit.setDeeplinkAndroid(itemRealm.getDeeplinkAndroid());
                toBeEdit.setDigest_uuid(itemRealm.getDigest_uuid());
                toBeEdit.setImages(itemRealm.getImages());
                toBeEdit.setIs_more_story(itemRealm.is_more_story());
                toBeEdit.setInfographs(itemRealm.getInfographs());
                toBeEdit.setImageProvider(itemRealm.getImageProvider());
                toBeEdit.setLink(itemRealm.getLink());
                toBeEdit.setLongreads(itemRealm.getLongreads());
                toBeEdit.setLocations(itemRealm.getLocations());
                toBeEdit.setMultiSummary(itemRealm.getMultiSummary());
                toBeEdit.setOrder(itemRealm.getOrder());
                toBeEdit.setPublished(itemRealm.getPublished());
                toBeEdit.setRelativeLink(itemRealm.getRelativeLink());
                toBeEdit.setStatDetail(itemRealm.getStatDetail());
                toBeEdit.setSources(itemRealm.getSources());
                toBeEdit.setSlideshow(itemRealm.getSlideshow());
                toBeEdit.setTumblrUrl(itemRealm.getTumblrUrl());
                toBeEdit.setTitle(itemRealm.getTitle());
                toBeEdit.setTweetKeywords(itemRealm.getTweetKeywords());
                toBeEdit.setTweets(itemRealm.getTweets());
                toBeEdit.setVideos(itemRealm.getVideos());
                toBeEdit.setWikis(itemRealm.getWikis());
                realm.commitTransaction();
                success = true;

            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }
}
