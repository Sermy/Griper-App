package com.griper.griperapp.homescreen.models;

/**
 * Created by Sarthak on 06-04-2017
 */

public class MyPostsMetaDataModel {
    private static int count;
    private static int nextPage;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        MyPostsMetaDataModel.count = count;
    }

    public static int getNextPage() {
        return nextPage;
    }

    public static void setNextPage(int nextPage) {
        MyPostsMetaDataModel.nextPage = nextPage;
    }
}
