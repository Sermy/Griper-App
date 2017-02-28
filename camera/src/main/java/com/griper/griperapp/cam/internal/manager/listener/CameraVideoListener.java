package com.griper.griperapp.cam.internal.manager.listener;

import java.io.File;

import com.griper.griperapp.cam.internal.utils.Size;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile);

    void onVideoRecordError();
}
