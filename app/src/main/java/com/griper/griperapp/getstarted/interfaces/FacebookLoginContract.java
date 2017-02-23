package com.griper.griperapp.getstarted.interfaces;

import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.griper.griperapp.getstarted.parsers.SignUpRequestDataParser;
import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;

import org.json.JSONException;

/**
 * Created by Sarthak on 21-02-2017
 */

public interface FacebookLoginContract {

    interface View {
        void init();

        void goToFacebookLogin();

        void showProgressBar(boolean show);

        void showFacebookErrorMessage(String errorMessage);

        boolean isViewDestroyed();
    }

    interface Presenter {
        void onFacebookLoginApiSuccess(LoginResult loginResult);

        void onFacebookLoginApiFailure(String errorMessage);

        void onGraphApiSuccess(LoginResult loginResult, GraphResponse response) throws JSONException;

        void onGraphApiFailure(String errorMessage);

        void callCreateProfileApi(SignUpRequestDataParser signUpRequestDataParser, LoginResult loginResult, GraphResponse graphResponse);

        void onCreateProfileApiSuccess(SignUpResponseParser responseParser, SignUpRequestDataParser requestDataParser);

        void onCreateProfileApiFailure(String errorMessage);

    }
}
