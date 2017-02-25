package com.griper.griperapp.getstarted.presenters;

import android.content.Context;

import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.getstarted.interfaces.EmailSignUpContract;
import com.griper.griperapp.getstarted.interfaces.GetStartedWebServiceInterface;
import com.griper.griperapp.getstarted.parsers.SignUpRequestDataParser;
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

public class EmailSignUpPresenter implements EmailSignUpContract.Presenter {

    /*
        Injecting the required stuff
     */
    @Inject
    Context context;

    @Inject
    GetStartedWebServiceInterface webServiceInterface;

    private final EmailSignUpContract.View view;

    public EmailSignUpPresenter(EmailSignUpContract.View view) {
        this.view = view;
    }


    @Override
    public void callCreateProfileApi(final SignUpRequestDataParser signUpRequestDataParser) {
        if (Utils.isNetworkAvailable(context)) {
            view.showProgressBar(true);
            webServiceInterface.createProfile(signUpRequestDataParser.getName(), signUpRequestDataParser.getEmail(),
                    signUpRequestDataParser.getIsfb(), signUpRequestDataParser.getPass(), signUpRequestDataParser.getUserdp())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends SignUpResponseParser>>() {
                        @Override
                        public Observable<? extends SignUpResponseParser> call(Throwable throwable) {
                            return null;
                        }
                    }).subscribe(new Subscriber<SignUpResponseParser>() {
                @Override
                public void onCompleted() {
                    Timber.i("Email SignUp Create Profile Api Success");
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e("Email SignUp Create Profile Api Failure : " + e.getMessage());
                    onCreateProfileApiFailure(e.getMessage());
                }

                @Override
                public void onNext(SignUpResponseParser responseParser) {
                    if (responseParser.getSuccess().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                        onCreateProfileApiSuccess(responseParser, signUpRequestDataParser);
                    } else if (responseParser.getSuccess().equals(AppConstants.EMAIL_ALREADY_EXISTS)) {
                        onCreateProfileApiFailure(responseParser.getMessage());
                    } else {
                        onCreateProfileApiFailure(responseParser.getMessage());
                    }
                }
            });
        } else {
            Utils.showSnackBar(view.getParentView(), context.getString(R.string.string_error_no_network));
        }
    }

    @Override
    public void onCreateProfileApiSuccess(SignUpResponseParser responseParser, SignUpRequestDataParser requestDataParser) {
        UserProfileData.saveUserData(requestDataParser, responseParser);
        UserProfileData userProfileData = UserProfileData.getUserData();
        Utils.showSnackBar(view.getParentView(), context.getString(R.string.string_sign_up_success));
        view.showProgressBar(false);
        if (userProfileData != null) {
            setUserLoggedinStatus(true);
        }

    }

    private void setUserLoggedinStatus(boolean isLoggedIn) {
        UserPreferencesData preferencesData = UserPreferencesData.getUserPreferencesData();
        preferencesData.setUserLoggedIn(isLoggedIn);
        Timber.i("User Logged in: " + isLoggedIn);
    }

    @Override
    public void onCreateProfileApiFailure(String errorMessage) {
        view.showProgressBar(false);
        Utils.showSnackBar(view.getParentView(), errorMessage);
    }
}
