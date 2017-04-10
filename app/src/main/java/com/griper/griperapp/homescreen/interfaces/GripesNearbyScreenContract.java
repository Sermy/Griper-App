package com.griper.griperapp.homescreen.interfaces;

import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;

import java.util.List;

import butterknife.Bind;

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

        void updateGripeAdapterLikes(int position, int likeCount, boolean isLike);

        boolean isViewDestroyed();
    }

    interface Presenter {

        void callGetNearbyGripesApi(int page);

        void onGetNearbyGripesApiSuccess(GripesNearbyResponseParser responseParser, boolean isNewPostsLoaded);

        void onGetNearbyGripesApiFailure(boolean isEmpty, boolean isFailure);

        void callUpdateLikesApi(String gripeId, boolean isLike);
    }
}
