package com.griper.griperapp.homescreen.interfaces;

/**
 * Created by Sarthak on 17-03-2017
 */

public interface ShowGripesDetailContract {

    interface View {
        void init();

        void showData();

        void setViewHighlighted(boolean show);

        void updateLikeCount(boolean isLiked, int likeCount);

        void animateLikeCount(boolean isLiked, int likeCount);

        void animateLikeButtonPressed(boolean isLiked, int likeCount);

        void syncLikeUpdateMainScreen(int position, int likeCount, boolean isLiked);
    }

    interface Presenter {
        void callUpdateGripeLikeApi(String gripeId, int position, int likeCount, boolean isLike);
    }
}
