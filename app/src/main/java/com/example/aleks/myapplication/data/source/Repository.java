package com.example.aleks.myapplication.data.source;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.aleks.myapplication.data.source.model.Item;
import com.example.aleks.myapplication.data.source.model.Store;
import com.example.aleks.myapplication.utils.URLUnshortener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;

@Singleton
public class Repository {
    private static final String TAG = Repository.class.getSimpleName();

    private Map<String, Item> cachedItems = new HashMap<>();
    private Map<String, Store> cachedStores = new HashMap<>();

    public Single<Item> loadItemData(String shortUrl) {
        return Single.create(emitter -> {
            try {
                if (cachedItems.containsKey(shortUrl)) {
                    emitter.onSuccess(cachedItems.get(shortUrl));
                    return;
                }
                Item item = new Item();
                item.setShortUrl(shortUrl);
                Document document = Jsoup.connect(shortUrl).followRedirects(true).get();
                Element name = document.select("h1.product-name").first();
                Element lowPrice = document.select("span[itemprop='lowPrice']").first();
                if (lowPrice != null) {
                    Element highPrice = document.select("span[itemprop='highPrice']").first();
                    item.setPriceLow(Double.parseDouble(lowPrice.html().replace("&nbsp;", "").replace(",", ".")));
                    item.setPriceHigh(Double.parseDouble(highPrice.html().replace("&nbsp;", "").replace(",", ".")));
                }
                else {
                    lowPrice = document.select("span#j-sku-discount-itemPrice").first();
                    if (lowPrice == null)
                        lowPrice = document.select("span#j-sku-discount-price").first();
                    if (lowPrice == null)
                        lowPrice = document.select("span#j-sku-itemPrice").first();
                    if (lowPrice == null)
                        lowPrice = document.select("span#j-sku-price").first();
                    item.setPriceLow(Double.parseDouble(lowPrice.html().replace("&nbsp;", "").replace(",", ".")));
                }

                item.setItemTitle(name.html().replace("&nbsp;", " "));

                Element img = document.select("img[data-role='thumb']").first();
                String imgSrc = img.attr("src");
                InputStream input = new java.net.URL(imgSrc).openStream();
                item.setItemBitmap(BitmapFactory.decodeStream(input));

                Element store = document.select("span.shop-name").first();
                if (store != null) {
                    int storeIdStartIndex = store.html().indexOf("aliexpress.com/store/") + 21;
                    int storeIdSearchEnd = Math.min(store.html().length(), storeIdStartIndex + 20);
                    int storeIdEndIndex = store.html().substring(storeIdStartIndex, storeIdSearchEnd).indexOf("?");
                    if (storeIdEndIndex == -1)
                        storeIdEndIndex = store.html().substring(storeIdStartIndex, storeIdSearchEnd).indexOf("\"");
                    if (storeIdEndIndex == -1)
                        storeIdEndIndex = store.html().length();
                    item.setItemStoreId(store.html().substring(storeIdStartIndex, storeIdStartIndex + storeIdEndIndex));
                }

                cachedItems.put(shortUrl, item);
                emitter.onSuccess(item);
            } catch (InterruptedIOException e) {
                if (!emitter.isDisposed())
                    emitter.onError(e);
            }
        });
    }

    public Single<Store> loadStoreDetails(String itemStoreId) {
        return Single.create(emitter -> {
            try {
                if (cachedStores.containsKey(itemStoreId)) {
                    emitter.onSuccess(cachedStores.get(itemStoreId));
                    return;
                }

                Store store = new Store();
                store.setStoreId(itemStoreId);
                String storeUrl = "http://aliexpress.com/store/feedback-score/" + itemStoreId + ".html";
                Document storeDoc = Jsoup.connect(storeUrl).followRedirects(true).get();

                int ownerMemberIdStartIndex = storeDoc.html().indexOf("ownerMemberId: '") + 16;
                int ownerMemberIdSearchEnd = Math.min(storeDoc.html().length(), ownerMemberIdStartIndex + 30);
                int ownerMemberIdLength = storeDoc.html().substring(ownerMemberIdStartIndex, ownerMemberIdSearchEnd).indexOf("'");
                if (ownerMemberIdLength == -1)
                    ownerMemberIdLength = 9;
                String ownerMemberId = storeDoc.html().substring(ownerMemberIdStartIndex, ownerMemberIdStartIndex + ownerMemberIdLength);

                String feedbackUrl = "https://feedback.aliexpress.com/display/evaluationDetail.htm?ownerMemberId=" + ownerMemberId;

                //feedback page with store info
                Document feedbackDoc = Jsoup.connect(feedbackUrl).followRedirects(true).get();

                //Seller Summary table
                Element tableFeedbackSummary = feedbackDoc.select("div#feedback-summary").first().child(1).child(0).child(0);
                store.setSellerTitle(tableFeedbackSummary.child(0).child(1).child(0).html());
                store.setSellerFeedbackScore(tableFeedbackSummary.child(2).child(1).child(0).html());
                store.setSellerSince(tableFeedbackSummary.child(3).child(1).html());

                //Detailed seller ratings table
                Element tableFeedbackDsr = feedbackDoc.select("div#feedback-dsr").first().child(1).child(0).child(0);
                store.setItemAsDescribed(tableFeedbackDsr.child(0).child(1).child(1).child(0).html());
                store.setCommunication(tableFeedbackDsr.child(1).child(1).child(1).child(0).html());
                store.setShippingSpeed(tableFeedbackDsr.child(2).child(1).child(1).child(0).html());

                //Feedback History table
                Element tableFeedbackHistory = feedbackDoc.select("div#feedback-history").first().child(1).child(0).child(0);
                store.setPositiveFeedbacks3m(tableFeedbackHistory.child(1).child(2).child(0).html());
                store.setNeutralFeedbacks3m(tableFeedbackHistory.child(2).child(2).child(0).html());
                store.setNegativeFeedbacks3m(tableFeedbackHistory.child(3).child(2).child(0).html());
                store.setPositiveFeedbackRate3m(tableFeedbackHistory.child(4).child(2).html());

                cachedStores.put(itemStoreId, store);
                emitter.onSuccess(store);
            } catch (InterruptedIOException e) {
                if (!emitter.isDisposed())
                    emitter.onError(e);
            }
        });
    }
}
