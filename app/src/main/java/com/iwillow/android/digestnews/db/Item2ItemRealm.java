package com.iwillow.android.digestnews.db;

import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.entity.StringRealmWrapper;
import com.iwillow.android.digestnews.entity.Tweet;
import com.iwillow.android.digestnews.entity.TweetEntity;
import com.iwillow.android.digestnews.entity.TweetEntityRealm;
import com.iwillow.android.digestnews.entity.TweetItem;
import com.iwillow.android.digestnews.entity.TweetItemRealm;
import com.iwillow.android.digestnews.entity.TweetRealm;
import com.iwillow.android.digestnews.entity.Wiki;
import com.iwillow.android.digestnews.entity.WikiRealm;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by https://www.github.com/iwillow on 2016/4/27.
 */
public class Item2ItemRealm {

    public static ItemRealm convert(Item item) {

        if (item == null) {
            return null;
        }
        ItemRealm itemRealm = new ItemRealm();
        itemRealm.setCategories(item.getCategories());
        itemRealm.setColors(item.getColors());
        itemRealm.setCluster_id(item.getCluster_id());
        itemRealm.setDeeplinkAndroid(item.getDeeplinkAndroid());
        itemRealm.setDigest_uuid(item.getDigest_uuid());
        itemRealm.setImageProvider(item.getImageProvider());
        itemRealm.setDeeplinkIOS(item.getDeeplinkIOS());
        itemRealm.setId(item.getId());
        itemRealm.setImages(item.getImages());
        itemRealm.setInfographs(item.getInfographs());
        itemRealm.setIs_more_story(item.is_more_story());
        itemRealm.setLink(item.getLink());
        itemRealm.setLocations(item.getLocations());
        itemRealm.setLongreads(item.getLongreads());

        itemRealm.setMultiSummary(item.getMultiSummary());

        List<String> orders = item.getOrder();
        if (orders != null && orders.size() > 0) {

            RealmList<StringRealmWrapper> orderForRealm = new RealmList<StringRealmWrapper>();
            for (String order : orders) {
                orderForRealm.add(new StringRealmWrapper().setValue(order));
            }
            itemRealm.setOrder(orderForRealm);
        }
        itemRealm.setPublished(item.getPublished());
        itemRealm.setRelativeLink(item.getRelativeLink());
        itemRealm.setSlideshow(item.getSlideshow());
        itemRealm.setSources(item.getSources());
        itemRealm.setStatDetail(item.getStatDetail());
        itemRealm.setTitle(item.getTitle());
        itemRealm.setTumblrUrl(item.getTumblrUrl());
        List<String> tweetWords = item.getTweetKeywords();
        if (tweetWords != null && tweetWords.size() > 0) {
            RealmList<StringRealmWrapper> tweetForRealm = new RealmList<StringRealmWrapper>();
            for (String tweet : orders) {
                tweetForRealm.add(new StringRealmWrapper().setValue(tweet));
            }
            itemRealm.setTweetKeywords(tweetForRealm);
        }
        Tweet tweet = item.getTweets();
        if (tweet != null) {
            TweetRealm tweetRealm = new TweetRealm();
            tweetRealm.setItem_id(tweet.getItem_id());
            tweetRealm.setUuid(tweet.getUuid());
            List<TweetItem> tweetItems = tweet.getTweets();
            if (tweetItems != null && tweetItems.size() > 0) {

                for (TweetItem tweetItem : tweetItems) {

                    TweetItemRealm tweetItemRealm = new TweetItemRealm();
                    tweetItemRealm.setCreated_at(tweetItem.getCreated_at());
                    tweetItemRealm.setId(tweetItem.getId());
                    tweetItemRealm.setText(tweetItem.getText());
                    tweetItemRealm.setPossibly_sensitive(tweetItem.getPossibly_sensitive());
                    tweetItemRealm.setUser(tweetItem.getUser());
                    TweetEntity tweetEntity = tweetItem.getEntities();
                    if (tweetEntity != null) {
                        TweetEntityRealm tweetEntityRealm = new TweetEntityRealm();
                        tweetEntityRealm.setMedia(tweetEntity.getMedia());
                        tweetEntityRealm.setUrls(tweetEntity.getUrls());

                        List<String> hashtags = tweetEntity.getHashtags();
                        if (hashtags != null) {
                            RealmList<StringRealmWrapper> realmHastags = new RealmList<StringRealmWrapper>();
                            for (String hashtag : hashtags) {
                                realmHastags.add(new StringRealmWrapper().setValue(hashtag));
                            }
                            tweetEntityRealm.setHashtags(realmHastags);
                        }

                        List<String> symbols = tweetEntity.getSymbols();
                        if (symbols != null) {
                            RealmList<StringRealmWrapper> realmSymbols = new RealmList<StringRealmWrapper>();
                            for (String symbol : symbols) {
                                realmSymbols.add(new StringRealmWrapper().setValue(symbol));
                            }
                            tweetEntityRealm.setSymbols(realmSymbols);
                        }

                        tweetItemRealm.setEntities(tweetEntityRealm);
                    }
                }

            }
            itemRealm.setTweets(tweetRealm);
        }
        itemRealm.setVideos(item.getVideos());
        List<Wiki> wikis = item.getWikis();
        if (wikis != null) {
            RealmList<WikiRealm> wikiRealms = new RealmList<WikiRealm>();
            for (Wiki wiki : wikis) {
                WikiRealm wikiRealm = new WikiRealm();
                wikiRealm.setId(wiki.getId());
                wikiRealm.setText(wiki.getText());
                wikiRealm.setImages(wiki.getImages());
                wikiRealm.setTitle(wiki.getTitle());
                List<String> terms = wiki.getSearchTerms();
                if (terms != null) {
                    RealmList<StringRealmWrapper> realmTerms = new RealmList<StringRealmWrapper>();
                    for (String term : terms) {
                        realmTerms.add(new StringRealmWrapper().setValue(term));
                    }
                    wikiRealm.setSearchTerms(realmTerms);
                }
                wikiRealm.setUrl(wiki.getUrl());
                wikiRealms.add(wikiRealm);
            }
            itemRealm.setWikis(wikiRealms);
        }
        return itemRealm;
    }
}
