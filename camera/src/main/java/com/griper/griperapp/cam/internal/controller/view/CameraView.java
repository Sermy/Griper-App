package com.griper.griperapp.cam.internal.controller.view;

import android.app.Activity;
import android.view.View;

import com.griper.griperapp.cam.internal.configuration.CamConfiguration;
import com.griper.griperapp.cam.internal.utils.Size;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraView {

    Activity getActivity();

    void updateCameraPreview(Size size, View cameraPreview);

    void updateUiForMediaAction(@CamConfiguration.MediaAction int mediaAction);

    void updateCameraSwitcher(int numberOfCameras);

    void onPhotoTaken();

    void onVideoRecordStart(int width, int height);

    void onVideoRecordStop();

    void releaseCameraPreview();

    void onCameraReady();
}
