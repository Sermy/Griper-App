package com.griper.griperapp.cam.internal.ui.model;

import com.griper.griperapp.cam.internal.configuration.CamConfiguration;
import com.griper.griperapp.cam.internal.utils.Size;

/**
 * Created by Sarthak on 26-02-2017
 */

public class PhotoQualityOption implements CharSequence {

    @CamConfiguration.MediaQuality
    private int mediaQuality;
    private String title;

    public PhotoQualityOption(@CamConfiguration.MediaQuality int mediaQuality, Size size) {
        this.mediaQuality = mediaQuality;

        title = String.valueOf(size.getWidth()) + " x " + String.valueOf(size.getHeight());
    }

    @CamConfiguration.MediaQuality
    public int getMediaQuality() {
        return mediaQuality;
    }

    @Override
    public int length() {
        return title.length();
    }

    @Override
    public char charAt(int index) {
        return title.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return title.subSequence(start, end);
    }

    @Override
    public String toString() {
        return title;
    }
}
