package com.griper.griperapp.cam.internal.configuration;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Sarthak on 26-02-2017
 */
public final class CamConfiguration {

    public static final int MEDIA_QUALITY_AUTO = 10;
    public static final int MEDIA_QUALITY_LOWEST = 15;
    public static final int MEDIA_QUALITY_LOW = 11;
    public static final int MEDIA_QUALITY_MEDIUM = 12;
    public static final int MEDIA_QUALITY_HIGH = 13;
    public static final int MEDIA_QUALITY_HIGHEST = 14;

    public static final int MEDIA_ACTION_VIDEO = 100;
    public static final int MEDIA_ACTION_PHOTO = 101;
    public static final int MEDIA_ACTION_UNSPECIFIED = 102;

    public static final int CAMERA_FACE_FRONT = 0x6;
    public static final int CAMERA_FACE_REAR = 0x7;

    public static final int SENSOR_POSITION_UP = 90;
    public static final int SENSOR_POSITION_UP_SIDE_DOWN = 270;
    public static final int SENSOR_POSITION_LEFT = 0;
    public static final int SENSOR_POSITION_RIGHT = 180;
    public static final int SENSOR_POSITION_UNSPECIFIED = -1;

    public static final int DISPLAY_ROTATION_0 = 0;
    public static final int DISPLAY_ROTATION_90 = 90;
    public static final int DISPLAY_ROTATION_180 = 180;
    public static final int DISPLAY_ROTATION_270 = 270;

    public static final int ORIENTATION_PORTRAIT = 0x111;
    public static final int ORIENTATION_LANDSCAPE = 0x222;

    public static final int FLASH_MODE_ON = 1;
    public static final int FLASH_MODE_OFF = 2;
    public static final int FLASH_MODE_AUTO = 3;

    public interface Arguments {
        String REQUEST_CODE = "com.memfis19.annca.request_code";
        String MEDIA_ACTION = "com.memfis19.annca.media_action";
        String MEDIA_QUALITY = "com.memfis19.annca.camera_media_quality";
        String VIDEO_DURATION = "com.memfis19.annca.video_duration";
        String MINIMUM_VIDEO_DURATION = "com.memfis19.annca.minimum.video_duration";
        String VIDEO_FILE_SIZE = "com.memfis19.annca.camera_video_file_size";
        String FLASH_MODE = "com.memfis19.annca.camera_flash_mode";
        String FILE_PATH = "com.memfis19.annca.camera_video_file_path";
    }

    @IntDef({MEDIA_QUALITY_AUTO, MEDIA_QUALITY_LOWEST, MEDIA_QUALITY_LOW, MEDIA_QUALITY_MEDIUM, MEDIA_QUALITY_HIGH, MEDIA_QUALITY_HIGHEST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaQuality {
    }

    @IntDef({MEDIA_ACTION_VIDEO, MEDIA_ACTION_PHOTO, MEDIA_ACTION_UNSPECIFIED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaAction {
    }

    @IntDef({FLASH_MODE_ON, FLASH_MODE_OFF, FLASH_MODE_AUTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlashMode {
    }

    @IntDef({CAMERA_FACE_FRONT, CAMERA_FACE_REAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraFace {
    }

    @IntDef({SENSOR_POSITION_UP, SENSOR_POSITION_UP_SIDE_DOWN, SENSOR_POSITION_LEFT, SENSOR_POSITION_RIGHT, SENSOR_POSITION_UNSPECIFIED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorPosition {
    }

    @IntDef({DISPLAY_ROTATION_0, DISPLAY_ROTATION_90, DISPLAY_ROTATION_180, DISPLAY_ROTATION_270})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayRotation {
    }

    @IntDef({ORIENTATION_PORTRAIT, ORIENTATION_LANDSCAPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceDefaultOrientation {
    }

    private Activity activity = null;
    private Fragment fragment = null;

    private int requestCode = -1;

    @MediaAction
    private int mediaAction = -1;

    @MediaQuality
    private int mediaQuality = -1;

    @CameraFace
    private int cameraFace = -1;

    private int videoDuration = -1;

    private long videoFileSize = -1;

    private int minimumVideoDuration = -1;

    @FlashMode
    private int flashMode = FLASH_MODE_AUTO;

    private CamConfiguration(Activity activity, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
    }

    private CamConfiguration(@NonNull Fragment fragment, int requestCode) {
        this.fragment = fragment;
        this.requestCode = requestCode;
    }

    public static class Builder {

        private CamConfiguration camConfiguration;


        public Builder(@NonNull Activity activity, @IntRange(from = 0) int requestCode) {
            camConfiguration = new CamConfiguration(activity, requestCode);
        }

        public Builder(@NonNull Fragment fragment, @IntRange(from = 0) int requestCode) {
            camConfiguration = new CamConfiguration(fragment, requestCode);
        }

        public Builder setMediaAction(@MediaAction int mediaAction) {
            camConfiguration.mediaAction = mediaAction;
            return this;
        }

        public Builder setMediaQuality(@MediaQuality int mediaQuality) {
            camConfiguration.mediaQuality = mediaQuality;
            return this;
        }

        /***
         * @param videoDurationInMilliseconds - video duration in milliseconds
         * @return
         */
        public Builder setVideoDuration(@IntRange(from = 1000, to = Integer.MAX_VALUE) int videoDurationInMilliseconds) {
            camConfiguration.videoDuration = videoDurationInMilliseconds;
            return this;
        }

        /***
         * @param minimumVideoDurationInMilliseconds - minimum video duration in milliseconds, used only in video mode
         *                                           for auto quality.
         * @return
         */
        public Builder setMinimumVideoDuration(@IntRange(from = 1000, to = Integer.MAX_VALUE) int minimumVideoDurationInMilliseconds) {
            camConfiguration.minimumVideoDuration = minimumVideoDurationInMilliseconds;
            return this;
        }

        /***
         * @param videoSizeInBytes - file size in bytes
         * @return
         */
        public Builder setVideoFileSize(@IntRange(from = 1048576, to = Long.MAX_VALUE) long videoSizeInBytes) {
            camConfiguration.videoFileSize = videoSizeInBytes;
            return this;
        }

        public Builder setFlashMode(@FlashMode int flashMode) {
            camConfiguration.flashMode = flashMode;
            return this;
        }

        public CamConfiguration build() throws IllegalArgumentException {
            if (camConfiguration.requestCode < 0)
                throw new IllegalArgumentException("Wrong request code value. Please set the value > 0.");
            if (camConfiguration.mediaQuality == MEDIA_QUALITY_AUTO && camConfiguration.minimumVideoDuration < 0) {
                throw new IllegalStateException("Please provide minimum video duration in milliseconds to use auto quality.");
            }

            return camConfiguration;
        }

    }

    public Activity getActivity() {
        return activity;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getMediaAction() {
        return mediaAction;
    }

    public int getMediaQuality() {
        return mediaQuality;
    }

    public int getCameraFace() {
        return cameraFace;
    }

    public int getVideoDuration() {
        return videoDuration;
    }

    public long getVideoFileSize() {
        return videoFileSize;
    }

    public int getMinimumVideoDuration() {
        return minimumVideoDuration;
    }

    public int getFlashMode() {
        return flashMode;
    }
}
