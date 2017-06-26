package com.example.aleks.myapplication;

import android.app.Application;
import android.content.Context;

import com.example.aleks.myapplication.data.source.DaggerRepositoryComponent;
import com.example.aleks.myapplication.data.source.RepositoryComponent;
import com.example.aleks.myapplication.data.source.RepositoryModule;
import com.squareup.leakcanary.LeakCanary;

public class AliTestApplication extends Application {

    private RepositoryComponent mRepositoryComponent;

    static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        mRepositoryComponent = DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public RepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
