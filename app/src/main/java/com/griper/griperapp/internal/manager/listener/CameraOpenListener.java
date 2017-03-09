package com.griper.griperapp.internal.manager.listener;

import com.griper.griperapp.internal.utils.Size;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraOpenListener<CameraId, SurfaceListener> {
    void onCameraOpened(CameraId openedCameraId, Size previewSize, SurfaceListener surfaceListener);

    void onCameraReady();

    void onCameraOpenError();
}
