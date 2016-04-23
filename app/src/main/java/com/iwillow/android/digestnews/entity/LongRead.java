package com.iwillow.android.digestnews.entity;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class LongRead {
    private String publisher;
    private String description;
    private String title;
    private Image images;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "LongRead{" +
                "publisher='" + publisher + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", url='" + url + '\'' +
                '}';
    }
}
