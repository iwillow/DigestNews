package com.iwillow.android.digestnews.db;

import android.text.TextUtils;

import com.iwillow.android.digestnews.entity.Color;
import com.iwillow.android.digestnews.entity.DetailItem;
import com.iwillow.android.digestnews.entity.Image;
import com.iwillow.android.digestnews.entity.ImageAsset;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.entity.Source;
import com.iwillow.android.digestnews.entity.Stream;
import com.iwillow.android.digestnews.entity.StringRealmWrapper;
import com.iwillow.android.digestnews.entity.Tweet;
import com.iwillow.android.digestnews.entity.TweetEntity;
import com.iwillow.android.digestnews.entity.TweetEntityRealm;
import com.iwillow.android.digestnews.entity.TweetItem;
import com.iwillow.android.digestnews.entity.TweetItemRealm;
import com.iwillow.android.digestnews.entity.TweetRealm;
import com.iwillow.android.digestnews.entity.Video;
import com.iwillow.android.digestnews.entity.Wiki;
import com.iwillow.android.digestnews.entity.WikiRealm;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by https://www.github.com/iwillow on 2016/4/27.
 */
public class EntityHelper {

    public static final int SIZE_SQUARE = 0;
    public static final int SIZE_SQUARE_LARGE = 1;
    public static final int SIZE_LARGE = 2;
    public static final int SIZE_EXTRA_LARGE = 3;
    public static final int SIZE_ORIGINAL = 4;

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

                RealmList<TweetItemRealm> tweetsList = new RealmList<TweetItemRealm>();

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
                    tweetsList.add(tweetItemRealm);
                }
                tweetRealm.setTweets(tweetsList);
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


    public static DetailItem ItemRealm2DetailItem(ItemRealm itemRealm) {

        DetailItem detailItem = new DetailItem();
        if (itemRealm == null) {
            return detailItem;
        }
        detailItem.checked = itemRealm.isChecked();
        Color color = itemRealm.getColors() == null ? null : itemRealm.getColors().get(0);
        detailItem.color = color == null ? android.graphics.Color.BLACK : android.graphics.Color.parseColor(color.getHexcode());
        detailItem.hexCode = color == null ? "#ff000000" : color.getHexcode();
        String label = itemRealm.getCategories() != null && !itemRealm.getCategories().isEmpty() ? itemRealm.getCategories().get(0).getLabel() : "World";
        detailItem.label = label;
        detailItem.uuid = itemRealm.getId();
        detailItem.title = itemRealm.getTitle();
        String img = "";
        if (!TextUtils.isEmpty(getImage(itemRealm.getImages(), SIZE_LARGE))) {
            img = getImage(itemRealm.getImages(), SIZE_LARGE);
        } else if (!TextUtils.isEmpty(getImage(itemRealm.getImages(), SIZE_SQUARE_LARGE))) {
            img = getImage(itemRealm.getImages(), SIZE_SQUARE_LARGE);
        } else if (!TextUtils.isEmpty(getImage(itemRealm.getImages(), SIZE_SQUARE))) {
            img = getImage(itemRealm.getImages(), SIZE_SQUARE);
        } else if (!TextUtils.isEmpty(getImage(itemRealm.getImages(), SIZE_EXTRA_LARGE))) {
            img = getImage(itemRealm.getImages(), SIZE_EXTRA_LARGE);
        } else if (!TextUtils.isEmpty(getImage(itemRealm.getImages(), SIZE_ORIGINAL))) {
            img = getImage(itemRealm.getImages(), SIZE_ORIGINAL);
        }
        detailItem.img = img;
        RealmList<Source> presses = itemRealm.getSources();
        StringBuilder sb = new StringBuilder();
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
        detailItem.press = sb.toString();
        StringBuilder od = new StringBuilder();
        RealmList<StringRealmWrapper> orders = itemRealm.getOrder();
        if (orders != null && orders.size() > 0) {
            for (StringRealmWrapper stringRealmWrapper : orders) {
                od.append(stringRealmWrapper.getValue()).append(",");
            }
        }
        detailItem.order = od.toString();
        return detailItem;
    }

    /**
     * get image
     *
     * @param image
     * @param sizeOption
     * @return
     */
    public static String getImage(Image image, int sizeOption) {
        String url = "";
        if (image != null) {
            if (sizeOption < 0) {
                sizeOption = SIZE_SQUARE;
            } else if (sizeOption > 4) {
                sizeOption = SIZE_LARGE;
            }

            if (sizeOption == SIZE_ORIGINAL) {
                if (image.getUrl() != null) {
                    url = image.getUrl();
                    return url;
                }
            }

            RealmList<ImageAsset> realmList = image.getImage_assets();
            for (ImageAsset imageAsset : realmList) {
                if (sizeOption == SIZE_ORIGINAL && "size=original".equalsIgnoreCase(imageAsset.getTag())) {
                    url = imageAsset.getUrl();
                    break;
                } else if (sizeOption == SIZE_LARGE && "pc:size=large".equalsIgnoreCase(imageAsset.getTag())) {
                    url = imageAsset.getUrl();
                    break;
                } else if (sizeOption == SIZE_SQUARE && "pc:size=square".equalsIgnoreCase(imageAsset.getTag())) {
                    url = imageAsset.getUrl();
                    break;
                } else if (sizeOption == SIZE_SQUARE_LARGE && "pc:size=square_large".equalsIgnoreCase(imageAsset.getTag())) {
                    url = imageAsset.getUrl();
                    break;
                } else if (sizeOption == SIZE_EXTRA_LARGE && "pc:size=extra_large".equalsIgnoreCase(imageAsset.getTag())) {
                    url = imageAsset.getUrl();
                    break;
                }
            }

        }
        return url;
    }

    public static String getImageSrc(Image image) {
        String img = "";
        if (!TextUtils.isEmpty(getImage(image, SIZE_LARGE))) {
            img = getImage(image, SIZE_LARGE);
        } else if (!TextUtils.isEmpty(getImage(image, SIZE_SQUARE_LARGE))) {
            img = getImage(image, SIZE_SQUARE_LARGE);
        } else if (!TextUtils.isEmpty(getImage(image, SIZE_SQUARE))) {
            img = getImage(image, SIZE_SQUARE);
        } else if (!TextUtils.isEmpty(getImage(image, SIZE_EXTRA_LARGE))) {
            img = getImage(image, SIZE_EXTRA_LARGE);
        } else if (!TextUtils.isEmpty(getImage(image, SIZE_ORIGINAL))) {
            img = getImage(image, SIZE_ORIGINAL);
        }
        return img;
    }

    public static String getVideoUrl(ItemRealm itemRealm) {
        String url = "";
        if (itemRealm != null && itemRealm.getVideos() != null) {
            RealmList<Video> videos = itemRealm.getVideos();
            for (Video video : videos) {
                RealmList<Stream> streams = video.getStreams();
                for (Stream stream : streams) {
                    if ("video/webm".equalsIgnoreCase(stream.getMime_type())) {
                        url = stream.getUrl();
                        return url;
                    }
                }
            }
        }

        return url;

    }

    public static boolean contains() {
        boolean img = false;
        return img;
    }
}
