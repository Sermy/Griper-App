package com.griper.griperapp.getstarted.interfaces;

import android.support.design.widget.CoordinatorLayout;

import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.griper.griperapp.getstarted.parsers.SignUpRequestDataParser;
import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;

/**
 * Created by Sarthak on 24-02-2017
 */

public interface EmailSignUpContract {

    interface View {
        void init();

        CoordinatorLayout getParentView();

        void setSignUpDetailsEnabled(boolean show);

        void showProgressBar(boolean show);
    }

    interface Presenter {
        void callCreateProfileApi(SignUpRequestDataParser signUpRequestDataParser);

        void onCreateProfileApiSuccess(SignUpResponseParser responseParser, SignUpRequestDataParser requestDataParser);

        void onCreateProfileApiFailure(String errorMessage);
    }
}
