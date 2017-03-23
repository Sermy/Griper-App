package com.griper.griperapp.homescreen.presenters;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.HomeScreenContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 27-02-2017
 */

public class HomeScreenPresenter implements HomeScreenContract.Presenter {

    private final HomeScreenContract.View homeScreenView;

    private UserProfileData userProfileData;

    public HomeScreenPresenter(HomeScreenContract.View view) {
        this.homeScreenView = view;
    }

}
