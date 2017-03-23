package com.griper.griperapp;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.activeandroid.app.Application;
import com.crashlytics.android.Crashlytics;
import com.griper.griperapp.injections.components.ApplicationComponent;
import com.griper.griperapp.injections.components.DaggerApplicationComponent;
import com.griper.griperapp.injections.modules.ApplicationModule;
import com.griper.griperapp.utils.CrashlyticsTree;

import io.fabric.sdk.android.Fabric;
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
        Fabric.with(this, new Crashlytics());
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
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
            Timber.plant(new CrashlyticsTree());
        }
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
