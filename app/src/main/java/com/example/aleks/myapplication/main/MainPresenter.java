package com.example.aleks.myapplication.main;

import com.example.aleks.myapplication.BasePresenter;
import com.example.aleks.myapplication.data.source.Repository;
import com.example.aleks.myapplication.data.source.model.Item;
import com.example.aleks.myapplication.data.source.model.Store;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter, BasePresenter<MainContract.View> {
    private Repository repository;
    private MainContract.View view;

    private DisposableSingleObserver<Item> itemDataObserver;
    private DisposableSingleObserver<Store> storeDataObserver;

    private String shortUrl;
    private Item item;
    private Store store;
    private boolean itemIsLoading;

    private static final Pattern shortAliUrlPattern = Pattern.compile("http://s.aliexpress.com/[a-zA-Z0-9_]+");

    public MainPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onViewAttached(MainContract.View view) {
        this.view = view;
        if (item != null)
            showItemDetails(item);
        if (store != null)
            showStoreDetails(store);
        if (itemIsLoading)
            view.showProgressDialog();
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroyed() {
        disposeAllObservers();
    }

    @Override
    public void loadData() {
        if (Objects.equals(shortUrl, "")) {
            view.showInputUrlError();
            return;
        }

        disposeAllObservers();
        item = null;
        store = null;

        itemIsLoading = true;
        view.hideItemDetails();
        view.hideStoreDetails();
        view.showProgressDialog();

        itemDataObserver = new DisposableSingleObserver<Item>() {
            @Override
            public void onSuccess(@NonNull Item item) {
                itemIsLoading = false;
                MainPresenter.this.item = item;
                showItemDetails(item);
                loadStoreDetails();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                itemIsLoading = false;
                e.printStackTrace();
                view.hideItemDetails();
                view.dismissProgressDialog();
                view.showSimpleLoadingError();
            }
        };

        repository.loadItemData(shortUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemDataObserver);
    }

    private void showItemDetails(@NonNull Item item) {
        view.setItemTitle(item.getItemTitle());
        view.setItemPrice(item.getItemPrice());
        view.setItemImage(item.getItemBitmap());
        view.dismissProgressDialog();
        view.showItemDetails();
        view.showStoreDetailsProgressBar();
    }

    private void showStoreDetails(Store store) {
        view.setSellerTitle(store.getSellerTitle());
        view.setSellerFeedbackScore(store.getSellerFeedbackScore());
        view.setSellerSince(store.getSellerSince());

        view.setSellerItemAsDescribed(store.getItemAsDescribed());
        view.setSellerCommunication(store.getCommunication());
        view.setSellerShippingSpeed(store.getShippingSpeed());

        view.setSellerPositive3m(store.getPositiveFeedbacks3m());
        view.setSellerNeutral3m(store.getNeutralFeedbacks3m());
        view.setSellerNegative3m(store.getNegativeFeedbacks3m());
        view.setSellerPositiveFeedbackRate(store.getPositiveFeedbackRate3m());

        view.hideStoreDetailsProgressBar();
        view.showStoreDetails();
    }

    @Override
    public void handleSharedText(String sharedText) {
        Matcher matcher = shortAliUrlPattern.matcher(sharedText);
        if (matcher.find()) {
            shortUrl = matcher.group();
            view.setShortUrl(shortUrl);
        }
    }

    public void setShortUrl(String s) {
        shortUrl = s;
    }

    private void loadStoreDetails() {
        if (storeDataObserver != null && !storeDataObserver.isDisposed()) {
            storeDataObserver.dispose();
        }

        storeDataObserver = new DisposableSingleObserver<Store>() {
            @Override
            public void onSuccess(Store store) {
                MainPresenter.this.store = store;
                showStoreDetails(store);
            }
            @Override
            public void onError(@NonNull Throwable throwable) {
                throwable.printStackTrace();
                view.hideStoreDetails();
                view.hideStoreDetailsProgressBar();
                view.showSimpleLoadingError();
            }
        };

        repository.loadStoreDetails(item.getItemStoreId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(storeDataObserver);
    }

    void disposeAllObservers() {
        if (storeDataObserver != null && !storeDataObserver.isDisposed()) {
            storeDataObserver.dispose();
        }
        if (itemDataObserver != null && !itemDataObserver.isDisposed()) {
            itemDataObserver.dispose();
        }
    }

}
