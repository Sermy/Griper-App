package com.griper.griperapp.internal.manager;

import android.content.Context;

import com.griper.griperapp.internal.configuration.CamConfiguration;
import com.griper.griperapp.internal.configuration.ConfigurationProvider;
import com.griper.griperapp.internal.manager.impl.CameraHandler;
import com.griper.griperapp.internal.manager.impl.ParametersHandler;
import com.griper.griperapp.internal.manager.listener.CameraCloseListener;
import com.griper.griperapp.internal.manager.listener.CameraOpenListener;
import com.griper.griperapp.internal.manager.listener.CameraPhotoListener;
import com.griper.griperapp.internal.manager.listener.CameraVideoListener;
import com.griper.griperapp.internal.utils.Size;

import java.io.File;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraManager<CameraId, SurfaceListener, CameraParameters, Camera> {

    void initializeCameraManager(ConfigurationProvider configurationProvider, Context context);

    void openCamera(CameraId cameraId, CameraOpenListener<CameraId, SurfaceListener> cameraOpenListener);

    void closeCamera(CameraCloseListener<CameraId> cameraCloseListener);

    void setFlashMode(@CamConfiguration.FlashMode int flashMode);

    void takePhoto(File photoFile, CameraPhotoListener cameraPhotoListener);

    void startVideoRecord(File videoFile, CameraVideoListener cameraVideoListener);

    Size getPhotoSizeForQuality(@CamConfiguration.MediaQuality int mediaQuality);

    void stopVideoRecord();

    void releaseCameraManager();

    CameraId getCurrentCameraId();

    CameraId getFaceFrontCameraId();

    CameraId getFaceBackCameraId();

    int getNumberOfCameras();

    int getFaceFrontCameraOrientation();

    int getFaceBackCameraOrientation();

    boolean isVideoRecording();

    boolean handleParameters(ParametersHandler<CameraParameters> parameters);

    void handleCamera(CameraHandler<Camera> cameraHandler);
}
