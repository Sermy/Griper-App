package com.griper.griperapp.getstarted.presenters;

import android.content.Context;

import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.getstarted.interfaces.EmailLoginContract;
import com.griper.griperapp.getstarted.interfaces.GetStartedWebServiceInterface;
import com.griper.griperapp.getstarted.parsers.LoginRequestDataParser;
import com.griper.griperapp.getstarted.parsers.LoginResponseParser;
import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 24-02-2017
 */

public class EmailLoginPresenter implements EmailLoginContract.Presenter {

    /*
        Injecting the required stuff
     */

    @Inject
    Context context;

    @Inject
    GetStartedWebServiceInterface webServiceInterface;

    private final EmailLoginContract.View view;

    public EmailLoginPresenter(EmailLoginContract.View view) {
        this.view = view;
    }

    @Override
    public void callLoginInApi(LoginRequestDataParser requestDataParser) {
        if (Utils.isNetworkAvailable(context)) {
            view.showProgressBar(true);
            webServiceInterface.signIn(requestDataParser).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends LoginResponseParser>>() {
                        @Override
                        public Observable<? extends LoginResponseParser> call(Throwable throwable) {
                            return null;
                        }
                    }).subscribe(new Subscriber<LoginResponseParser>() {
                @Override
                public void onCompleted() {
                    Timber.i("Call Login Api Success");
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e("Login Api Failure: " + e.getMessage());
                    onEmailLoginApiFailure(e.getMessage());
                }

                @Override
                public void onNext(LoginResponseParser loginResponseParser) {
                    if (loginResponseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                        onEmailLoginApiSuccess(loginResponseParser);
                    } else {
                        onEmailLoginApiFailure(loginResponseParser.getMessage());
                    }
                }
            });

        } else {
            Utils.showSnackBar(view.getParentView(), context.getString(R.string.string_error_no_network));
        }
    }

    @Override
    public void onEmailLoginApiSuccess(LoginResponseParser loginResponseParser) {
        view.showProgressBar(false);
        UserProfileData.saveUserData(loginResponseParser);
        UserProfileData userProfileData = UserProfileData.getUserData();
        if (userProfileData != null) {
            setUserLoggedinStatus(true);
        }
        view.showHomeScreen();
    }

    private void setUserLoggedinStatus(boolean isLoggedIn) {
        UserPreferencesData preferencesData = UserPreferencesData.getUserPreferencesData();
        preferencesData.setUserLoggedIn(isLoggedIn);
        Timber.i("User Logged in: " + isLoggedIn);
    }


    @Override
    public void onEmailLoginApiFailure(String errorMessage) {
        Utils.showSnackBar(view.getParentView(), errorMessage);
        view.showProgressBar(false);
        view.setLoginDetailsEnabled(true);
    }
}
