package com.iwillow.android.digestnews.entity;

/**
 * Created by https://github.com/iwillow on 2016/4/23.
 */
public class Color {
    private String r;
    private String g;
    private String b;
    private String hexcode;
    private String tag;

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getHexcode() {
        return hexcode;
    }

    public void setHexcode(String hexcode) {
        this.hexcode = hexcode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Color{" +
                "r='" + r + '\'' +
                ", g='" + g + '\'' +
                ", b='" + b + '\'' +
                ", hexcode='" + hexcode + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
