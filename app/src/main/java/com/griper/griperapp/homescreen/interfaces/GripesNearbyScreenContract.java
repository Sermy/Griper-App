package com.griper.griperapp.homescreen.interfaces;

import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;

import java.util.List;

/**
 * Created by Sarthak on 17-03-2017
 */

public interface GripesNearbyScreenContract {

    interface View {
        void init();

        void showPosts(List<GripesDataModel> gripesDataModelList);

        void showNewPosts(List<GripesDataModel> gripesDataModelList);

        void showProgressBar(boolean show);

        void showLoadMoreProgressBar(boolean show);

        boolean isViewDestroyed();
    }

    interface Presenter {

        void callGetNearbyGripesApi(int page);

        void onGetNearbyGripesApiSuccess(GripesNearbyResponseParser responseParser, boolean isNewPostsLoaded);

        void onGetNearbyGripesApiFailure(boolean isEmpty, boolean isFailure);
    }
}
