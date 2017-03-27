package com.griper.griperapp.homescreen.models;

/**
 * Created by Sarthak on 26-03-2017
 */

public class GripesMetaDataModel {

    private static int count;
    private static int nextPage;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        GripesMetaDataModel.count = count;
    }

    public static int getNextPage() {
        return nextPage;
    }

    public static void setNextPage(int nextPage) {
        GripesMetaDataModel.nextPage = nextPage;
    }
}
