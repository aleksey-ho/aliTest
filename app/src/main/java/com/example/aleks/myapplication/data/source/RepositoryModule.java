package com.example.aleks.myapplication.data.source;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    Repository provideRepository()
    {
        return new Repository();
    }

}
