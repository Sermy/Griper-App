package com.griper.griperapp.homescreen.interfaces;

import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;

import java.util.List;

/**
 * Created by Sarthak on 11-04-2017
 */

public interface ShowMyLikesContract {

    interface View {
        void init();

        boolean isViewDestroyed();

        void showLikedPosts(List<GripesDataModel> myLikedPostsList);

        void showMoreLikedPosts(List<GripesDataModel> myLoadMoreLikedPostsList);

        void showProgressBar(boolean show);

        void showLoadMoreProgressBar(boolean show);
    }

    interface Presenter {
        void callMyLikesApi(int page);

        void onMyLikedPostsApiSuccess(GripesNearbyResponseParser responseParser, boolean isNewLikedPostsLoaded);

        void onMyLikedPostsFailure();
    }

}
