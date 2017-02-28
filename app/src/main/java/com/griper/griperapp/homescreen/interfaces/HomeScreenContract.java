package com.griper.griperapp.homescreen.interfaces;

/**
 * Created by Sarthak on 27-02-2017
 */

public interface HomeScreenContract {

    interface View {
        void init();

        void enableCamera();

        void goToFacebookLoginScreen();
    }

    interface Presenter {

    }
}
