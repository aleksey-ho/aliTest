package com.example.aleks.myapplication;

public interface PresenterFactory<T extends BasePresenter> {
    T create();
}