package com.griper.griperapp.homescreen.interfaces;

import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;

import java.util.List;

/**
 * Created by Sarthak on 05-04-2017
 */

public interface ShowMyPostsContract {

    interface View {
        void init();

        boolean isViewDestroyed();

        void showPosts(List<GripesDataModel> myPostsList);

        void showNewPosts(List<GripesDataModel> myLoadMorePostsList);

        void showProgressBar(boolean show);

        void showLoadMoreProgressBar(boolean show);
    }

    interface Presenter {
        void callMyPostsApi(int page);

        void onMyPostsApiSuccess(GripesNearbyResponseParser responseParser, boolean isNewPostsLoaded);

        void onMyPostsFailure();
    }
}
