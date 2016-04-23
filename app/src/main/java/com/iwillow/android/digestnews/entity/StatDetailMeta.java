package com.iwillow.android.digestnews.entity;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class StatDetailMeta {
    private String text;
    private String color;
    private boolean enabled;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "StatDetailMeta{" +
                "text='" + text + '\'' +
                ", color='" + color + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
