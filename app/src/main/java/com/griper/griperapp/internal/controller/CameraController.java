package com.griper.griperapp.internal.controller;

import android.os.Bundle;

import com.griper.griperapp.internal.configuration.CamConfiguration;
import com.griper.griperapp.internal.manager.CameraManager;

import java.io.File;

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
