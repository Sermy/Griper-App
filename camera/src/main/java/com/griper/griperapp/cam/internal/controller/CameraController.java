package com.griper.griperapp.cam.internal.controller;

import android.os.Bundle;

import java.io.File;

import com.griper.griperapp.cam.internal.configuration.CamConfiguration;
import com.griper.griperapp.cam.internal.manager.CameraManager;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraController<CameraId> {

    void onCreate(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onDestroy();

    void takePhoto();

    void startVideoRecord();

    void stopVideoRecord();

    boolean isVideoRecording();

    void switchCamera(@CamConfiguration.CameraFace int cameraFace);

    void switchQuality();

    void setFlashMode(@CamConfiguration.FlashMode int flashMode);

    int getNumberOfCameras();

    @CamConfiguration.MediaAction
    int getMediaAction();

    CameraId getCurrentCameraId();

    File getOutputFile();

    CameraManager getCameraManager();
}
