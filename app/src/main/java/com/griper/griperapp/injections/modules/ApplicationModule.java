package com.griper.griperapp.injections.modules;

import android.content.Context;

import com.griper.griperapp.BaseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sarthak on 19-02-2017
 */
@Module
public class ApplicationModule {
    private BaseApplication application;

    public ApplicationModule(BaseApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return application;
    }
}
