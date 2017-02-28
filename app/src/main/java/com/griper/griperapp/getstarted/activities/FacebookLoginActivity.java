package com.griper.griperapp.getstarted.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.getstarted.interfaces.FacebookLoginContract;
import com.griper.griperapp.getstarted.presenters.FacebookLoginPresenter;
import com.griper.griperapp.homescreen.activities.HomeScreenActivity;
import com.griper.griperapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;
import timber.log.Timber;

/**
 * Created by Sarthak on 20-02-2017
 */

public class FacebookLoginActivity extends BaseActivity implements FacebookLoginContract.View {

    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int CAPTURE_MEDIA = 368;

    @Bind(R.id.layoutProgressBar)
    protected LinearLayout progressBar;

    private CallbackManager callbackManager;
    private ArrayList<String> fbPermissions = new ArrayList<>();

    private FacebookLoginContract.Presenter facebookLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        init();
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        facebookLoginPresenter = new FacebookLoginPresenter(this);
        getApiComponent().inject((FacebookLoginPresenter) facebookLoginPresenter);
    }

    @OnClick(R.id.linearLayoutFacebook)
    public void onClickFacebookLogin() {
        goToFacebookLogin();
    }

    /*
        Called as a result of default FacebookActivity to request callback
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.buttonEmail)
    public void onClickEmailLogin() {
        Intent intent = new Intent(FacebookLoginActivity.this, EmailProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToFacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Timber.i("FB login success: " + loginResult.getAccessToken().getToken());
                        //Call Graph Api
                        facebookLoginPresenter.onFacebookLoginApiSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Timber.e("FB login cancel");
                        facebookLoginPresenter.onFacebookLoginApiFailure(getString(R.string.string_error_facebook_login_cancelled));
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Timber.e("FB login error: " + Log.getStackTraceString(error));
                        facebookLoginPresenter.onFacebookLoginApiFailure(getString(R.string.string_error_facebook_login));
                    }
                });

        if (fbPermissions.isEmpty()) {
            fbPermissions.addAll(Arrays.asList(getResources().getStringArray(R.array.string_array_fb_permissions)));
        }
        if (Utils.isNetworkAvailable(this)) {
            showProgressBar(true);
            LoginManager.getInstance().logInWithReadPermissions(this, fbPermissions);
        } else {
            showFacebookErrorMessage(getString(R.string.string_error_no_network));
        }
    }

    @Override
    public void showProgressBar(boolean show) {
        if (!isViewDestroyed()) {
            if (show) {
                if (progressBar.getVisibility() != View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showFacebookErrorMessage(String errorMessage) {
        if (!isViewDestroyed()) {
            showProgressBar(false);
            Utils.showToast(this, errorMessage);
        }
    }

    @Override
    public void showHomeScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isViewDestroyed()) {
                    Intent intent = new Intent(FacebookLoginActivity.this, HomeScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });

    }

    @Override
    public boolean isViewDestroyed() {
        return isFinishing();
    }

}
