package com.griper.griperapp.internal.ui.camera;

import android.media.CamcorderProfile;

import com.griper.griperapp.internal.configuration.CamConfiguration;
import com.griper.griperapp.internal.configuration.ConfigurationProvider;
import com.griper.griperapp.internal.controller.CameraController;
import com.griper.griperapp.internal.controller.impl.Camera1Controller;
import com.griper.griperapp.internal.controller.view.CameraView;
import com.griper.griperapp.internal.ui.BaseCamActivity;
import com.griper.griperapp.internal.ui.model.PhotoQualityOption;
import com.griper.griperapp.internal.ui.model.VideoQualityOption;
import com.griper.griperapp.internal.utils.CameraHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 26-02-2017
 */
@SuppressWarnings("deprecation")
public class Camera1Activity extends BaseCamActivity<Integer> {

    @Override
    public CameraController<Integer> createCameraController(CameraView cameraView, ConfigurationProvider configurationProvider) {
        return new Camera1Controller(cameraView, configurationProvider);
    }

    @Override
    protected CharSequence[] getVideoQualityOptions() {
        List<CharSequence> videoQualities = new ArrayList<>();

        if (getMinimumVideoDuration() > 0)
            videoQualities.add(new VideoQualityOption(CamConfiguration.MEDIA_QUALITY_AUTO, CameraHelper.getCamcorderProfile(CamConfiguration.MEDIA_QUALITY_AUTO, getCameraController().getCurrentCameraId()), getMinimumVideoDuration()));

        CamcorderProfile camcorderProfile = CameraHelper.getCamcorderProfile(CamConfiguration.MEDIA_QUALITY_HIGH, getCameraController().getCurrentCameraId());
        double videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(CamConfiguration.MEDIA_QUALITY_HIGH, camcorderProfile, videoDuration));

        camcorderProfile = CameraHelper.getCamcorderProfile(CamConfiguration.MEDIA_QUALITY_MEDIUM, getCameraController().getCurrentCameraId());
        videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(CamConfiguration.MEDIA_QUALITY_MEDIUM, camcorderProfile, videoDuration));

        camcorderProfile = CameraHelper.getCamcorderProfile(CamConfiguration.MEDIA_QUALITY_LOW, getCameraController().getCurrentCameraId());
        videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(CamConfiguration.MEDIA_QUALITY_LOW, camcorderProfile, videoDuration));

        CharSequence[] array = new CharSequence[videoQualities.size()];
        videoQualities.toArray(array);

        return array;
    }

    @Override
    protected CharSequence[] getPhotoQualityOptions() {
        List<CharSequence> photoQualities = new ArrayList<>();
        photoQualities.add(new PhotoQualityOption(CamConfiguration.MEDIA_QUALITY_HIGHEST, getCameraController().getCameraManager().getPhotoSizeForQuality(CamConfiguration.MEDIA_QUALITY_HIGHEST)));
        photoQualities.add(new PhotoQualityOption(CamConfiguration.MEDIA_QUALITY_HIGH, getCameraController().getCameraManager().getPhotoSizeForQuality(CamConfiguration.MEDIA_QUALITY_HIGH)));
        photoQualities.add(new PhotoQualityOption(CamConfiguration.MEDIA_QUALITY_MEDIUM, getCameraController().getCameraManager().getPhotoSizeForQuality(CamConfiguration.MEDIA_QUALITY_MEDIUM)));
        photoQualities.add(new PhotoQualityOption(CamConfiguration.MEDIA_QUALITY_LOWEST, getCameraController().getCameraManager().getPhotoSizeForQuality(CamConfiguration.MEDIA_QUALITY_LOWEST)));

        CharSequence[] array = new CharSequence[photoQualities.size()];
        photoQualities.toArray(array);

        return array;
    }

}
