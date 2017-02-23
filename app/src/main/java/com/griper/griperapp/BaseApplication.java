package com.griper.griperapp;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.activeandroid.app.Application;
import com.griper.griperapp.injections.components.ApplicationComponent;
import com.griper.griperapp.injections.components.DaggerApplicationComponent;
import com.griper.griperapp.injections.modules.ApplicationModule;

import timber.log.Timber;

/**
 * Created by Sarthak on 19-02-2017
 */

public class BaseApplication extends com.activeandroid.app.Application {

    private int resumeCount;
    private int pauseCount;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTimber();
        initializeApplicationComponent();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initializeApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void initializeTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    public void updateResumeCount() {
        this.resumeCount++;
    }

    public void updatePauseCount() {
        this.pauseCount++;
    }

    public int getResumeCount() {
        return resumeCount;
    }

    public int getPauseCount() {
        return pauseCount;
    }
}
