package com.example.aleks.myapplication;

/**
 * Created by aleks on 26.06.2017.
 */

public interface BasePresenter<V> {
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}