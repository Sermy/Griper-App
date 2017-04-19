package com.griper.griperapp.homescreen.models;

/**
 * Created by Sarthak on 19-04-2017
 */

public class CommentsUpdateModel {

    private static boolean commentIsPosted = false;

    public static boolean getCommentIsPosted() {
        return commentIsPosted;
    }

    public static void setCommentIsPosted(boolean commentIsPosted) {
        CommentsUpdateModel.commentIsPosted = commentIsPosted;
    }
}
