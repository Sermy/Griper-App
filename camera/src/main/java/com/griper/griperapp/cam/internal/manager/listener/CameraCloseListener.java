package com.griper.griperapp.cam.internal.manager.listener;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraCloseListener<CameraId> {
    void onCameraClosed(CameraId closedCameraId);
}
