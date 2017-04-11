package com.griper.griperapp.injections.components;

import com.griper.griperapp.getstarted.presenters.EmailLoginPresenter;
import com.griper.griperapp.getstarted.presenters.EmailSignUpPresenter;
import com.griper.griperapp.getstarted.presenters.FacebookLoginPresenter;
import com.griper.griperapp.homescreen.presenters.GripesMapScreenPresenter;
import com.griper.griperapp.homescreen.presenters.GripesNearbyScreenPresenter;
import com.griper.griperapp.homescreen.presenters.ShowGripeDetailPresenter;
import com.griper.griperapp.homescreen.presenters.ShowMyLikesPresenter;
import com.griper.griperapp.homescreen.presenters.ShowMyPostsPresenter;
import com.griper.griperapp.injections.modules.ApiModule;
import com.griper.griperapp.internal.ui.preview.AddGripePresenter;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Sarthak on 19-02-2017
 */
@Singleton
@Subcomponent(modules = {ApiModule.class})
public interface ApiComponent {

    void inject(FacebookLoginPresenter facebookLoginPresenter);

    void inject(EmailSignUpPresenter emailSignUpPresenter);

    void inject(EmailLoginPresenter emailLoginPresenter);

    void inject(AddGripePresenter presenter);

    void inject(GripesMapScreenPresenter gripesMapScreenPresenter);

    void inject(GripesNearbyScreenPresenter gripesNearbyScreenPresenter);

    void inject(ShowMyPostsPresenter showMyPostsPresenter);

    void inject(ShowMyLikesPresenter showMyLikesPresenter);

    void inject(ShowGripeDetailPresenter showGripeDetailPresenter);
}
