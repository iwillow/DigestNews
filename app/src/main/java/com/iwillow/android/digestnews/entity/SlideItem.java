package com.iwillow.android.digestnews.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by https://www.github.com/iwillow on 2016/5/8.
 */
public class SlideItem extends RealmObject implements Serializable, Parcelable {
    public String headline;
    public String provider_name;
    public String caption;
    public String url;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public SlideItem() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headline);
        dest.writeString(provider_name);
        dest.writeString(caption);
        dest.writeString(url);


    }

    public static final Parcelable.Creator<SlideItem> CREATOR = new Parcelable.Creator<SlideItem>()

    {
        @Override
        public SlideItem createFromParcel(Parcel source) {
            return new SlideItem(source);
        }

        @Override
        public SlideItem[] newArray(int size) {
            return new SlideItem[size];
        }
    };

    public SlideItem(Parcel in) {
        headline = in.readString();
        provider_name = in.readString();
        caption = in.readString();
        url = in.readString();
    }
}
