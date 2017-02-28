package com.griper.griperapp.cam;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;

import com.griper.griperapp.cam.internal.configuration.CamConfiguration;
import com.griper.griperapp.cam.internal.ui.camera.Camera1Activity;
import com.griper.griperapp.cam.internal.ui.camera2.Camera2Activity;
import com.griper.griperapp.cam.internal.utils.CameraHelper;

/**
 * Created by Sarthak on 26-02-2017
 */
public class Cam {

    private CamConfiguration camConfiguration;

    /***
     * Creates Cam instance with default configuration set to photo with medium quality.
     *
     * @param activity    - fromList which request was invoked
     * @param requestCode - request code which will return in onActivityForResult
     */
    public Cam(Activity activity, @IntRange(from = 0) int requestCode) {
        CamConfiguration.Builder builder = new CamConfiguration.Builder(activity, requestCode);
        camConfiguration = builder.build();
    }

    public Cam(Fragment fragment, @IntRange(from = 0) int requestCode) {
        CamConfiguration.Builder builder = new CamConfiguration.Builder(fragment, requestCode);
        camConfiguration = builder.build();
    }

    /***
     * Creates Cam instance with custom camera configuration.
     *
     * @param cameraConfiguration
     */
    public Cam(CamConfiguration cameraConfiguration) {
        this.camConfiguration = cameraConfiguration;
    }


    @RequiresPermission(Manifest.permission.CAMERA)
    public void launchCamera() {
        if (camConfiguration == null || (camConfiguration.getActivity() == null && camConfiguration.getFragment() == null))
            return;

        Intent cameraIntent;

        if (CameraHelper.hasCamera2(camConfiguration.getActivity())) {
            if (camConfiguration.getFragment() != null)
                cameraIntent = new Intent(camConfiguration.getFragment().getContext(), Camera2Activity.class);
            else cameraIntent = new Intent(camConfiguration.getActivity(), Camera2Activity.class);
        } else {
            if (camConfiguration.getFragment() != null)
                cameraIntent = new Intent(camConfiguration.getFragment().getContext(), Camera1Activity.class);
            else cameraIntent = new Intent(camConfiguration.getActivity(), Camera1Activity.class);
        }

        cameraIntent.putExtra(CamConfiguration.Arguments.REQUEST_CODE, camConfiguration.getRequestCode());

        if (camConfiguration.getMediaAction() > 0)
            cameraIntent.putExtra(CamConfiguration.Arguments.MEDIA_ACTION, camConfiguration.getMediaAction());

        if (camConfiguration.getMediaQuality() > 0)
            cameraIntent.putExtra(CamConfiguration.Arguments.MEDIA_QUALITY, camConfiguration.getMediaQuality());

        if (camConfiguration.getVideoDuration() > 0)
            cameraIntent.putExtra(CamConfiguration.Arguments.VIDEO_DURATION, camConfiguration.getVideoDuration());

        if (camConfiguration.getVideoFileSize() > 0)
            cameraIntent.putExtra(CamConfiguration.Arguments.VIDEO_FILE_SIZE, camConfiguration.getVideoFileSize());

        if (camConfiguration.getMinimumVideoDuration() > 0)
            cameraIntent.putExtra(CamConfiguration.Arguments.MINIMUM_VIDEO_DURATION, camConfiguration.getMinimumVideoDuration());

        cameraIntent.putExtra(CamConfiguration.Arguments.FLASH_MODE, camConfiguration.getFlashMode());

        if (camConfiguration.getFragment() != null) {

            camConfiguration.getFragment().startActivityForResult(cameraIntent, camConfiguration.getRequestCode());
        } else {
            camConfiguration.getActivity().startActivityForResult(cameraIntent, camConfiguration.getRequestCode());
        }
    }
}
