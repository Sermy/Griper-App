package com.griper.griperapp.homescreen.models;

/**
 * Created by Sarthak on 11-04-2017
 */

public class MyLikesMetaDataModel {
    private static int count;
    private static int nextPage;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        MyLikesMetaDataModel.count = count;
    }

    public static int getNextPage() {
        return nextPage;
    }

    public static void setNextPage(int nextPage) {
        MyLikesMetaDataModel.nextPage = nextPage;
    }
}