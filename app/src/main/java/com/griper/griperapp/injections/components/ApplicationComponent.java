package com.griper.griperapp.injections.components;

import android.content.Context;

import com.griper.griperapp.BaseApplication;
import com.griper.griperapp.injections.modules.ApiModule;
import com.griper.griperapp.injections.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sarthak on 19-02-2017
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Context getApplicationContext();

    ApiComponent plusApiComponent(ApiModule apiModule);

    void inject(BaseApplication application);
}
