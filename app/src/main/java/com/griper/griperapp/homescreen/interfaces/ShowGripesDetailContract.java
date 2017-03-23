package com.griper.griperapp.homescreen.interfaces;

/**
 * Created by Sarthak on 17-03-2017
 */

public interface ShowGripesDetailContract {

    interface View {
        void init();

        void showData();

        void setViewHighlighted(boolean show);
    }

    interface Presenter {

    }
}
