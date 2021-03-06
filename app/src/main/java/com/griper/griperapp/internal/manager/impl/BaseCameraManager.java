package com.griper.griperapp.internal.manager.impl;

import android.content.Context;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.griper.griperapp.internal.configuration.CamConfiguration;
import com.griper.griperapp.internal.configuration.ConfigurationProvider;
import com.griper.griperapp.internal.manager.CameraManager;
import com.griper.griperapp.internal.utils.Size;

/**
 * Created by Sarthak on 26-02-2017
 */
abstract class BaseCameraManager<CameraId, SurfaceListener, CameraParameters, Camera>
        implements CameraManager<CameraId, SurfaceListener, CameraParameters, Camera>, MediaRecorder.OnInfoListener {

    private static final String TAG = "BaseCameraManager";

    protected Context context;
    ConfigurationProvider configurationProvider;

    MediaRecorder videoRecorder;
    boolean isVideoRecording = false;

    CameraId currentCameraId = null;
    CameraId faceFrontCameraId = null;
    CameraId faceBackCameraId = null;
    int numberOfCameras = 0;
    int faceFrontCameraOrientation;
    int faceBackCameraOrientation;

    CamcorderProfile camcorderProfile;
    Size photoSize;
    Size videoSize;
    Size previewSize;
    Size windowSize;

    HandlerThread backgroundThread;
    Handler backgroundHandler;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void initializeCameraManager(ConfigurationProvider configurationProvider, Context context) {
        this.context = context;
        this.configurationProvider = configurationProvider;
        startBackgroundThread();
    }

    @Override
    public void releaseCameraManager() {
        this.context = null;
        stopBackgroundThread();
    }

    protected abstract void prepareCameraOutputs();

    protected abstract boolean prepareVideoRecorder();

    protected abstract void onMaxDurationReached();

    protected abstract void onMaxFileSizeReached();

    protected abstract int getPhotoOrientation(@CamConfiguration.SensorPosition int sensorPosition);

    protected abstract int getVideoOrientation(@CamConfiguration.SensorPosition int sensorPosition);

    protected void releaseVideoRecorder() {
        try {
            if (videoRecorder != null) {
                videoRecorder.reset();
                videoRecorder.release();
            }
        } catch (Exception ignore) {

        } finally {
            videoRecorder = null;
        }
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (Build.VERSION.SDK_INT > 17) {
            backgroundThread.quitSafely();
        } else backgroundThread.quit();

        try {
            backgroundThread.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "stopBackgroundThread: ", e);
        } finally {
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
        if (MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED == what) {
            onMaxDurationReached();
        } else if (MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED == what) {
            onMaxFileSizeReached();
        }
    }

    public boolean isVideoRecording() {
        return isVideoRecording;
    }

    public CameraId getCurrentCameraId() {
        return currentCameraId;
    }

    public CameraId getFaceFrontCameraId() {
        return faceFrontCameraId;
    }

    public CameraId getFaceBackCameraId() {
        return faceBackCameraId;
    }

    public int getNumberOfCameras() {
        return numberOfCameras;
    }

    public int getFaceFrontCameraOrientation() {
        return faceFrontCameraOrientation;
    }

    public int getFaceBackCameraOrientation() {
        return faceBackCameraOrientation;
    }


}
