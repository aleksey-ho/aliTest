package com.example.aleks.myapplication.data.source;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class})
public interface RepositoryComponent {

    Repository getRepository();
}
