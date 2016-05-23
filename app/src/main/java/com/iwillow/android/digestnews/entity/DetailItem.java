package com.iwillow.android.digestnews.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by https://www.github.com.com/iwillow on 2016/5/19.
 */
public class DetailItem implements Serializable, Parcelable {
    public int color;
    public boolean checked;
    public String uuid;
    public String title;
    public String label;
    public String press;
    public String order;
    public String hexCode;
    public String img;

    public DetailItem() {

    }

    public DetailItem(Parcel source) {
        color = source.readInt();
        // index = source.readInt();
        boolean value[] = new boolean[1];
        source.readBooleanArray(value);
        checked = value[0];
        uuid = source.readString();
        title = source.readString();
        label = source.readString();
        press = source.readString();
        order = source.readString();
        hexCode = source.readString();
        img = source.readString();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(color);
        boolean value[] = {checked};
        dest.writeBooleanArray(value);
        dest.writeString(uuid);
        dest.writeString(title);
        dest.writeString(label);
        dest.writeString(press);
        dest.writeString(order);
        dest.writeString(hexCode);
        dest.writeString(img);
    }

    public static final Parcelable.Creator<DetailItem> CREATOR = new Parcelable.Creator<DetailItem>()

    {
        @Override
        public DetailItem createFromParcel(Parcel source) {
            return new DetailItem(source);
        }

        @Override
        public DetailItem[] newArray(int size) {
            return new DetailItem[size];
        }
    };
}
