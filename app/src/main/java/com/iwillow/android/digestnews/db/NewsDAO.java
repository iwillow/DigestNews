package com.iwillow.android.digestnews.db;

import com.iwillow.android.digestnews.entity.ItemRealm;

import java.util.List;

import io.realm.Realm;


/**
 * Created by httos://www.github.com/iwillow on 2016/4/27.
 */
public interface NewsDAO {


    ItemRealm addNews(ItemRealm itemRealm, Realm realm);

    boolean deleteNews(String id, Realm realm);

    List<ItemRealm> queryNews(String id, Realm realm);

    boolean updateNews(ItemRealm itemRealm, Realm realm);
}
