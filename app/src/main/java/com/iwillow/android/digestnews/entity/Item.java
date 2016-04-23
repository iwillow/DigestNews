package com.iwillow.android.digestnews.entity;

import java.util.List;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class Item {
    private String digest_uuid;
    private String id;
    private String title;
    private String cluster_id;
    private String imageProvider;
    private String published;
    private String tumblrUrl;
    private String link;
    private List<String> order;
    private List<Location> locations;
    private List<Color> colors;
    private List<Infograph> infographs;
    private List<Category> categories;
    private List<Summary> multiSummary;
    private List<Wiki> wikis;
    private Image images;
    private List<LongRead> longreads;
    private List<Video> videos;
    private List<String> tweetKeywords;
    private List<Source> sources;
    private List<StatDetail> statDetail;
    private Slideshow slideshow;
    private Tweet tweets;
    private String deeplinkIOS;
    private String deeplinkAndroid;
    private boolean is_more_story;
    private String relativeLink;

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

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public List<Infograph> getInfographs() {
        return infographs;
    }

    public void setInfographs(List<Infograph> infographs) {
        this.infographs = infographs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Summary> getMultiSummary() {
        return multiSummary;
    }

    public void setMultiSummary(List<Summary> multiSummary) {
        this.multiSummary = multiSummary;
    }

    public List<Wiki> getWikis() {
        return wikis;
    }

    public void setWikis(List<Wiki> wikis) {
        this.wikis = wikis;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public List<LongRead> getLongreads() {
        return longreads;
    }

    public void setLongreads(List<LongRead> longreads) {
        this.longreads = longreads;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<String> getTweetKeywords() {
        return tweetKeywords;
    }

    public void setTweetKeywords(List<String> tweetKeywords) {
        this.tweetKeywords = tweetKeywords;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public List<StatDetail> getStatDetail() {
        return statDetail;
    }

    public void setStatDetail(List<StatDetail> statDetail) {
        this.statDetail = statDetail;
    }

    public Slideshow getSlideshow() {
        return slideshow;
    }

    public void setSlideshow(Slideshow slideshow) {
        this.slideshow = slideshow;
    }

    public Tweet getTweets() {
        return tweets;
    }

    public void setTweets(Tweet tweets) {
        this.tweets = tweets;
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

    @Override
    public String toString() {
        return "Item{" +
                "digest_uuid='" + digest_uuid + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", cluster_id='" + cluster_id + '\'' +
                ", imageProvider='" + imageProvider + '\'' +
                ", published='" + published + '\'' +
                ", tumblrUrl='" + tumblrUrl + '\'' +
                ", link='" + link + '\'' +
                ", order=" + order +
                ", locations=" + locations +
                ", colors=" + colors +
                ", infographs=" + infographs +
                ", categories=" + categories +
                ", multiSummary=" + multiSummary +
                ", wikis=" + wikis +
                ", images=" + images +
                ", longreads=" + longreads +
                ", videos=" + videos +
                ", tweetKeywords=" + tweetKeywords +
                ", sources=" + sources +
                ", statDetail=" + statDetail +
                ", slideshow=" + slideshow +
                ", tweets=" + tweets +
                ", deeplinkIOS='" + deeplinkIOS + '\'' +
                ", deeplinkAndroid='" + deeplinkAndroid + '\'' +
                ", is_more_story=" + is_more_story +
                ", relativeLink='" + relativeLink + '\'' +
                '}';
    }
}
