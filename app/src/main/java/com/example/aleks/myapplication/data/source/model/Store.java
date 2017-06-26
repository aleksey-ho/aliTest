package com.example.aleks.myapplication.data.source.model;

/**
 * Created by aleks on 25.06.2017.
 */

public class Store {
    private String storeId;
    private String sellerTitle, sellerFeedbackScore, sellerSince;
    private String itemAsDescribed, communication, shippingSpeed;
    private String positiveFeedbacks3m, neutralFeedbacks3m, negativeFeedbacks3m, positiveFeedbackRate3m;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSellerTitle() {
        return sellerTitle;
    }

    public void setSellerTitle(String sellerTitle) {
        this.sellerTitle = sellerTitle;
    }

    public String getSellerFeedbackScore() {
        return sellerFeedbackScore;
    }

    public void setSellerFeedbackScore(String sellerFeedbackScore) {
        this.sellerFeedbackScore = sellerFeedbackScore;
    }

    public String getSellerSince() {
        return sellerSince;
    }

    public void setSellerSince(String sellerSince) {
        this.sellerSince = sellerSince;
    }

    public String getItemAsDescribed() {
        return itemAsDescribed;
    }

    public void setItemAsDescribed(String itemAsDescribed) {
        this.itemAsDescribed = itemAsDescribed;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getShippingSpeed() {
        return shippingSpeed;
    }

    public void setShippingSpeed(String shippingSpeed) {
        this.shippingSpeed = shippingSpeed;
    }

    public String getPositiveFeedbacks3m() {
        return positiveFeedbacks3m;
    }

    public void setPositiveFeedbacks3m(String positiveFeedbacks3m) {
        this.positiveFeedbacks3m = positiveFeedbacks3m;
    }

    public String getNeutralFeedbacks3m() {
        return neutralFeedbacks3m;
    }

    public void setNeutralFeedbacks3m(String neutralFeedbacks3m) {
        this.neutralFeedbacks3m = neutralFeedbacks3m;
    }

    public String getNegativeFeedbacks3m() {
        return negativeFeedbacks3m;
    }

    public void setNegativeFeedbacks3m(String negativeFeedbacks3m) {
        this.negativeFeedbacks3m = negativeFeedbacks3m;
    }

    public String getPositiveFeedbackRate3m() {
        return positiveFeedbackRate3m;
    }

    public void setPositiveFeedbackRate3m(String positiveFeedbackRate3m) {
        this.positiveFeedbackRate3m = positiveFeedbackRate3m;
    }
}
