package com.example.aleks.myapplication.main;

import com.example.aleks.myapplication.AliTestApplication;
import com.example.aleks.myapplication.PresenterFactory;
import com.example.aleks.myapplication.data.source.Repository;

import static com.example.aleks.myapplication.AliTestApplication.getAppContext;

public class MainPresenterFactory implements PresenterFactory<MainPresenter> {

    @Override
    public MainPresenter create() {
        Repository repository = ((AliTestApplication)getAppContext()).getRepositoryComponent().getRepository();
        return new MainPresenter(repository);
    }
}