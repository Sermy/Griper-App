package com.griper.griperapp.homescreen.interfaces;

import android.os.Bundle;

import com.griper.griperapp.homescreen.models.FeaturedGripesModel;
import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 14-03-2017
 */

public interface GripesMapScreenContract {

    interface View {
        void init();

        void setupViewPager();

        void setProgressCircleLayoutVisibility(boolean show);

        void updateGripesMapMarkers(List<GripesMapResponseParser> responseParserList);

        void updateCameraPositionGripes(int position);

        public void updateGripeLikes(int position, int size, int likeCount, boolean isLiked);
    }

    interface Presenter {
        void callGetNearbyGripesApi();

        ArrayList<FeaturedGripesModel> getFeaturesGripesModel(List<GripesMapResponseParser> responseParserList);
    }
}
