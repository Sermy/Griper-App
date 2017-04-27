package com.griper.griperapp.internal.ui.preview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 25-04-2017
 */

public class ImagePreviewList {

    public static List<String> previewImagesList = new ArrayList<>();

    public static void addItems(String item) {
        previewImagesList.add(item);
    }

    public static void removeAll() {
        previewImagesList.clear();
    }
}
