package com.griper.griperapp.homescreen.interfaces;

/**
 * Created by Sarthak on 27-02-2017
 */

public interface HomeScreenContract {

    interface View {
        void init();

        void findDimensions();

        void setupViewPager();

        void enableCamera();

        void disableHomeNavigationSelected();

        void startIntentService(double latitude, double longitude);
    }

    interface Presenter {

    }
}
