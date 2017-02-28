package com.griper.griperapp.getstarted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.getstarted.interfaces.SplashScreenContract;
import com.griper.griperapp.homescreen.activities.HomeScreenActivity;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

/**
 * Created by Sarthak on 23-02-2017
 */

public class SplashActivity extends BaseActivity implements SplashScreenContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        goToNextScreen();
    }

    public void goToNextScreen() {
        UserPreferencesData preferencesData = UserPreferencesData.getUserPreferencesData();
        UserProfileData userProfileData = UserProfileData.getUserData();

        if (!preferencesData.isUserLoggedIn()) {
            showFacebookLoginScreen();
        } else {
            showHomeScreen();
        }
    }

    @Override
    public void showFacebookLoginScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Intent intent = new Intent(SplashActivity.this, FacebookLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, AppConstants.SPLASH_SCREEN_DURATION);
    }

    @Override
    public void showHomeScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Intent intent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, AppConstants.SPLASH_SCREEN_DURATION);
    }
}
