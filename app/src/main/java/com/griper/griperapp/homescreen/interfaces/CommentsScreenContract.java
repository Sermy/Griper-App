package com.griper.griperapp.homescreen.interfaces;

/**
 * Created by Sarthak on 16-04-2017
 */

public interface CommentsScreenContract {

    interface View {
        void init();
    }

    interface Presenter {
        void callUpdateGripeCommentsCount(String gripeId);
    }
}

