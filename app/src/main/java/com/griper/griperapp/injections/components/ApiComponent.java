package com.griper.griperapp.injections.components;

import com.griper.griperapp.getstarted.presenters.FacebookLoginPresenter;
import com.griper.griperapp.injections.modules.ApiModule;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Sarthak on 19-02-2017
 */
@Singleton
@Subcomponent(modules = {ApiModule.class})
public interface ApiComponent {

    void inject(FacebookLoginPresenter facebookLoginPresenter);
}
