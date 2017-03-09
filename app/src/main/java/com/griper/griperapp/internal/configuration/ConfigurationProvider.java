package com.griper.griperapp.internal.configuration;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface ConfigurationProvider {

    int getRequestCode();

    @CamConfiguration.MediaAction
    int getMediaAction();

    @CamConfiguration.MediaQuality
    int getMediaQuality();

    int getVideoDuration();

    long getVideoFileSize();

    @CamConfiguration.SensorPosition
    int getSensorPosition();

    int getDegrees();

    int getMinimumVideoDuration();

    @CamConfiguration.FlashMode
    int getFlashMode();
}
