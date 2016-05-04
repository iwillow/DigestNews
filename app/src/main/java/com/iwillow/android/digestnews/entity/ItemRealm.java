package com.iwillow.android.digestnews.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/4/27.
 */
public class ItemRealm extends RealmObject {


    @PrimaryKey
    private String id;
    private boolean checked;
    private String digest_uuid;
    private String title;
    private String cluster_id;
    private String imageProvider;
    private String published;
    private String tumblrUrl;
    private String link;
    private String deeplinkIOS;
    private String deeplinkAndroid;
    private boolean is_more_story;
    private String relativeLink;
    private Image images;
    private Slideshow slideshow;
    private TweetRealm tweets;

    private RealmList<StringRealmWrapper> order;
    private RealmList<Location> locations;
    private RealmList<Color> colors;
    private RealmList<Infograph> infographs;
    private RealmList<Category> categories;
    private RealmList<Summary> multiSummary;
    private RealmList<WikiRealm> wikis;
    private RealmList<LongRead> longreads;
    private RealmList<Video> videos;
    private RealmList<StringRealmWrapper> tweetKeywords;
    private RealmList<Source> sources;
    private RealmList<StatDetail> statDetail;


    public String getDigest_uuid() {
        return digest_uuid;
    }

    public void setDigest_uuid(String digest_uuid) {
        this.digest_uuid = digest_uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public String getImageProvider() {
        return imageProvider;
    }

    public void setImageProvider(String imageProvider) {
        this.imageProvider = imageProvider;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getTumblrUrl() {
        return tumblrUrl;
    }

    public void setTumblrUrl(String tumblrUrl) {
        this.tumblrUrl = tumblrUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeeplinkIOS() {
        return deeplinkIOS;
    }

    public void setDeeplinkIOS(String deeplinkIOS) {
        this.deeplinkIOS = deeplinkIOS;
    }

    public String getDeeplinkAndroid() {
        return deeplinkAndroid;
    }

    public void setDeeplinkAndroid(String deeplinkAndroid) {
        this.deeplinkAndroid = deeplinkAndroid;
    }

    public boolean is_more_story() {
        return is_more_story;
    }

    public void setIs_more_story(boolean is_more_story) {
        this.is_more_story = is_more_story;
    }

    public String getRelativeLink() {
        return relativeLink;
    }

    public void setRelativeLink(String relativeLink) {
        this.relativeLink = relativeLink;
    }

    public RealmList<StringRealmWrapper> getOrder() {
        return order;
    }

    public void setOrder(RealmList<StringRealmWrapper> order) {
        this.order = order;
    }

    public RealmList<Location> getLocations() {
        return locations;
    }

    public void setLocations(RealmList<Location> locations) {
        this.locations = locations;
    }

    public RealmList<Color> getColors() {
        return colors;
    }

    public void setColors(RealmList<Color> colors) {
        this.colors = colors;
    }

    public RealmList<Infograph> getInfographs() {
        return infographs;
    }

    public void setInfographs(RealmList<Infograph> infographs) {
        this.infographs = infographs;
    }

    public RealmList<Category> getCategories() {
        return categories;
    }

    public void setCategories(RealmList<Category> categories) {
        this.categories = categories;
    }

    public RealmList<Summary> getMultiSummary() {
        return multiSummary;
    }

    public void setMultiSummary(RealmList<Summary> multiSummary) {
        this.multiSummary = multiSummary;
    }

    public RealmList<WikiRealm> getWikis() {
        return wikis;
    }

    public void setWikis(RealmList<WikiRealm> wikis) {
        this.wikis = wikis;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public RealmList<LongRead> getLongreads() {
        return longreads;
    }

    public void setLongreads(RealmList<LongRead> longreads) {
        this.longreads = longreads;
    }

    public RealmList<Video> getVideos() {
        return videos;
    }

    public void setVideos(RealmList<Video> videos) {
        this.videos = videos;
    }

    public RealmList<StringRealmWrapper> getTweetKeywords() {
        return tweetKeywords;
    }

    public void setTweetKeywords(RealmList<StringRealmWrapper> tweetKeywords) {
        this.tweetKeywords = tweetKeywords;
    }

    public RealmList<Source> getSources() {
        return sources;
    }

    public void setSources(RealmList<Source> sources) {
        this.sources = sources;
    }

    public RealmList<StatDetail> getStatDetail() {
        return statDetail;
    }

    public void setStatDetail(RealmList<StatDetail> statDetail) {
        this.statDetail = statDetail;
    }

    public Slideshow getSlideshow() {
        return slideshow;
    }

    public void setSlideshow(Slideshow slideshow) {
        this.slideshow = slideshow;
    }

    public TweetRealm getTweets() {
        return tweets;
    }

    public void setTweets(TweetRealm tweets) {
        this.tweets = tweets;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "ItemRealm{" +
                "id='" + id + '\'' +
                ", checked=" + checked +
                ", digest_uuid='" + digest_uuid + '\'' +
                ", title='" + title + '\'' +
                ", cluster_id='" + cluster_id + '\'' +
                ", imageProvider='" + imageProvider + '\'' +
                ", published='" + published + '\'' +
                ", tumblrUrl='" + tumblrUrl + '\'' +
                ", link='" + link + '\'' +
                ", deeplinkIOS='" + deeplinkIOS + '\'' +
                ", deeplinkAndroid='" + deeplinkAndroid + '\'' +
                ", is_more_story=" + is_more_story +
                ", relativeLink='" + relativeLink + '\'' +
                ", images=" + images +
                ", slideshow=" + slideshow +
                ", tweets=" + tweets +
                ", order=" + order +
                ", locations=" + locations +
                ", colors=" + colors +
                ", infographs=" + infographs +
                ", categories=" + categories +
                ", multiSummary=" + multiSummary +
                ", wikis=" + wikis +
                ", longreads=" + longreads +
                ", videos=" + videos +
                ", tweetKeywords=" + tweetKeywords +
                ", sources=" + sources +
                ", statDetail=" + statDetail +
                '}';
    }
}
