package com.griper.griperapp.internal.manager.listener;

import com.griper.griperapp.internal.utils.Size;

import java.io.File;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile);

    void onVideoRecordError();
}
