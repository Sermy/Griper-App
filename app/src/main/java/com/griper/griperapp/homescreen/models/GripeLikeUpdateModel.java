package com.griper.griperapp.homescreen.models;

/**
 * Created by Sarthak on 09-04-2017
 */

public class GripeLikeUpdateModel {
    private int likeCount;
    private boolean isLiked;

    public GripeLikeUpdateModel() {
    }

    public GripeLikeUpdateModel(int likeCount, boolean isLiked) {
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
