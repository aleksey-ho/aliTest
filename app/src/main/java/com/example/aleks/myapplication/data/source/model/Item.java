package com.example.aleks.myapplication.data.source.model;

import android.graphics.Bitmap;

/**
 * Created by aleks on 25.06.2017.
 */

public class Item {

    private String shortUrl;
    private String url;
    private String itemTitle;
    private String itemStoreId;
    private Bitmap itemBitmap;
    private Double priceLow;
    private Double priceHigh;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemPrice() {
        String s = priceLow.toString();
        if (priceHigh != null)
            s += " - " + priceHigh.toString();
        return s;
    }

    public void setPriceLow(Double priceLow) {
        this.priceLow = priceLow;
    }

    public void setPriceHigh(Double priceHigh) {
        this.priceHigh = priceHigh;
    }

    public String getItemStoreId() {
        return itemStoreId;
    }

    public void setItemStoreId(String itemStoreId) {
        this.itemStoreId = itemStoreId;
    }

    public Bitmap getItemBitmap() {
        return itemBitmap;
    }

    public void setItemBitmap(Bitmap itemBitmap) {
        this.itemBitmap = itemBitmap;
    }
}