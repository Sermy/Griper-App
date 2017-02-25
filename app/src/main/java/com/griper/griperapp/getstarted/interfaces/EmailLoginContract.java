package com.griper.griperapp.getstarted.interfaces;

import android.support.design.widget.CoordinatorLayout;

import com.griper.griperapp.getstarted.parsers.LoginRequestDataParser;
import com.griper.griperapp.getstarted.parsers.LoginResponseParser;

/**
 * Created by Sarthak on 24-02-2017
 */

public interface EmailLoginContract {

    interface View {
        void init();

        void setLoginDetailsEnabled(boolean show);

        CoordinatorLayout getParentView();

        void showProgressBar(boolean show);
    }

    interface Presenter {
        void callLoginInApi(LoginRequestDataParser requestDataParser);

        void onEmailLoginApiSuccess(LoginResponseParser loginResponseParser);

        void onEmailLoginApiFailure(String errorMessage);
    }
}
