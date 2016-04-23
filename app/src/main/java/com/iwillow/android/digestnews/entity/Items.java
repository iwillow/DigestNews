package com.iwillow.android.digestnews.entity;

import java.util.List;

/**
 * Created by https://www.github.com/iwillow on 2016/4/23.
 */
public class Items {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Items{" +
                "items=" + items +
                '}';
    }
}
