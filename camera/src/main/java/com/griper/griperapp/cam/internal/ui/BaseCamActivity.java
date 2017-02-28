package com.griper.griperapp.cam.internal.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.griper.griperapp.cam.R;
import com.griper.griperapp.cam.internal.configuration.CamConfiguration;
import com.griper.griperapp.cam.internal.ui.model.PhotoQualityOption;
import com.griper.griperapp.cam.internal.ui.model.VideoQualityOption;
import com.griper.griperapp.cam.internal.ui.preview.PreviewActivity;
import com.griper.griperapp.cam.internal.ui.view.CameraControlPanel;
import com.griper.griperapp.cam.internal.ui.view.CameraSwitchView;
import com.griper.griperapp.cam.internal.ui.view.FlashSwitchView;
import com.griper.griperapp.cam.internal.ui.view.MediaActionSwitchView;
import com.griper.griperapp.cam.internal.ui.view.RecordButton;
import com.griper.griperapp.cam.internal.utils.Size;
import com.griper.griperapp.cam.internal.utils.Utils;

/**
 * Created by Sarthak on 26-02-2017
 */

public abstract class BaseCamActivity<CameraId> extends CamCameraActivity<CameraId>
        implements
        RecordButton.RecordButtonListener,
        FlashSwitchView.FlashModeSwitchListener,
        MediaActionSwitchView.OnMediaActionStateChangeListener,
        CameraSwitchView.OnCameraTypeChangeListener, CameraControlPanel.SettingsClickListener {

    private CameraControlPanel cameraControlPanel;
    private AlertDialog settingsDialog;

    protected static final int REQUEST_PREVIEW_CODE = 1001;

    public static final int ACTION_CONFIRM = 900;
    public static final int ACTION_RETAKE = 901;
    public static final int ACTION_CANCEL = 902;

    protected int requestCode = -1;

    @CamConfiguration.MediaAction
    protected int mediaAction = CamConfiguration.MEDIA_ACTION_UNSPECIFIED;
    @CamConfiguration.MediaQuality
    protected int mediaQuality = CamConfiguration.MEDIA_QUALITY_MEDIUM;
    @CamConfiguration.MediaQuality
    protected int passedMediaQuality = CamConfiguration.MEDIA_QUALITY_MEDIUM;

    @CamConfiguration.FlashMode
    protected int flashMode = CamConfiguration.FLASH_MODE_AUTO;

    protected CharSequence[] videoQualities;
    protected CharSequence[] photoQualities;

    protected int videoDuration = -1;
    protected long videoFileSize = -1;
    protected int minimumVideoDuration = -1;

    @MediaActionSwitchView.MediaActionState
    protected int currentMediaActionState;

    @CameraSwitchView.CameraType
    protected int currentCameraType = CameraSwitchView.CAMERA_TYPE_REAR;

    @CamConfiguration.MediaQuality
    protected int newQuality = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onProcessBundle(Bundle savedInstanceState) {
        super.onProcessBundle(savedInstanceState);

        extractConfiguration(getIntent().getExtras());
        currentMediaActionState = mediaAction == CamConfiguration.MEDIA_ACTION_VIDEO ?
                MediaActionSwitchView.ACTION_VIDEO : MediaActionSwitchView.ACTION_PHOTO;
    }

    @Override
    protected void onCameraControllerReady() {
        super.onCameraControllerReady();

        videoQualities = getVideoQualityOptions();
        photoQualities = getPhotoQualityOptions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);
    }

    private void extractConfiguration(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(CamConfiguration.Arguments.REQUEST_CODE))
                requestCode = bundle.getInt(CamConfiguration.Arguments.REQUEST_CODE);

            if (bundle.containsKey(CamConfiguration.Arguments.MEDIA_ACTION)) {
                switch (bundle.getInt(CamConfiguration.Arguments.MEDIA_ACTION)) {
                    case CamConfiguration.MEDIA_ACTION_PHOTO:
                        mediaAction = CamConfiguration.MEDIA_ACTION_PHOTO;
                        break;
                    case CamConfiguration.MEDIA_ACTION_VIDEO:
                        mediaAction = CamConfiguration.MEDIA_ACTION_VIDEO;
                        break;
                    default:
                        mediaAction = CamConfiguration.MEDIA_ACTION_UNSPECIFIED;
                        break;
                }
            }

            if (bundle.containsKey(CamConfiguration.Arguments.MEDIA_QUALITY)) {
                switch (bundle.getInt(CamConfiguration.Arguments.MEDIA_QUALITY)) {
                    case CamConfiguration.MEDIA_QUALITY_AUTO:
                        mediaQuality = CamConfiguration.MEDIA_QUALITY_AUTO;
                        break;
                    case CamConfiguration.MEDIA_QUALITY_HIGHEST:
                        mediaQuality = CamConfiguration.MEDIA_QUALITY_HIGHEST;
                        break;
                    case CamConfiguration.MEDIA_QUALITY_HIGH:
                        mediaQuality = CamConfiguration.MEDIA_QUALITY_HIGH;
                        break;
                    case CamConfiguration.MEDIA_QUALITY_MEDIUM:
                        mediaQuality = CamConfiguration.MEDIA_QUALITY_MEDIUM;
                        break;
                    case CamConfiguration.MEDIA_QUALITY_LOW:
                        mediaQuality = CamConfiguration.MEDIA_QUALITY_LOW;
                        break;
                    case CamConfiguration.MEDIA_QUALITY_LOWEST:
                        mediaQuality = CamConfiguration.MEDIA_QUALITY_LOWEST;
                        break;
                    default:
                        mediaQuality = CamConfiguration.MEDIA_QUALITY_MEDIUM;
                        break;
                }
                passedMediaQuality = mediaQuality;
            }

            if (bundle.containsKey(CamConfiguration.Arguments.VIDEO_DURATION))
                videoDuration = bundle.getInt(CamConfiguration.Arguments.VIDEO_DURATION);

            if (bundle.containsKey(CamConfiguration.Arguments.VIDEO_FILE_SIZE))
                videoFileSize = bundle.getLong(CamConfiguration.Arguments.VIDEO_FILE_SIZE);

            if (bundle.containsKey(CamConfiguration.Arguments.MINIMUM_VIDEO_DURATION))
                minimumVideoDuration = bundle.getInt(CamConfiguration.Arguments.MINIMUM_VIDEO_DURATION);

            if (bundle.containsKey(CamConfiguration.Arguments.FLASH_MODE))
                switch (bundle.getInt(CamConfiguration.Arguments.FLASH_MODE)) {
                    case CamConfiguration.FLASH_MODE_AUTO:
                        flashMode = CamConfiguration.FLASH_MODE_AUTO;
                        break;
                    case CamConfiguration.FLASH_MODE_ON:
                        flashMode = CamConfiguration.FLASH_MODE_ON;
                        break;
                    case CamConfiguration.FLASH_MODE_OFF:
                        flashMode = CamConfiguration.FLASH_MODE_OFF;
                        break;
                    default:
                        flashMode = CamConfiguration.FLASH_MODE_AUTO;
                        break;
                }
        }
    }

    @Override
    protected View getUserContentView(LayoutInflater layoutInflater, ViewGroup parent) {
        cameraControlPanel = (CameraControlPanel) layoutInflater.inflate(R.layout.user_control_layout, parent, false);

        if (cameraControlPanel != null) {
            cameraControlPanel.setup(getMediaAction());

            switch (flashMode) {
                case CamConfiguration.FLASH_MODE_AUTO:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_AUTO);
                    break;
                case CamConfiguration.FLASH_MODE_ON:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_ON);
                    break;
                case CamConfiguration.FLASH_MODE_OFF:
                    cameraControlPanel.setFlasMode(FlashSwitchView.FLASH_OFF);
                    break;
            }

            cameraControlPanel.setRecordButtonListener(this);
            cameraControlPanel.setFlashModeSwitchListener(this);
            cameraControlPanel.setOnMediaActionStateChangeListener(this);
            cameraControlPanel.setOnCameraTypeChangeListener(this);
            cameraControlPanel.setMaxVideoDuration(getVideoDuration());
            cameraControlPanel.setMaxVideoFileSize(getVideoFileSize());
            cameraControlPanel.setSettingsClickListener(this);
        }

        return cameraControlPanel;
    }

    @Override
    public void onSettingsClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (currentMediaActionState == MediaActionSwitchView.ACTION_VIDEO) {
            builder.setSingleChoiceItems(videoQualities, getVideoOptionCheckedIndex(), getVideoOptionSelectedListener());
            if (getVideoFileSize() > 0)
                builder.setTitle(String.format(getString(R.string.settings_video_quality_title),
                        "(Max " + String.valueOf(getVideoFileSize() / (1024 * 1024) + " MB)")));
            else
                builder.setTitle(String.format(getString(R.string.settings_video_quality_title), ""));
        } else {
            builder.setSingleChoiceItems(photoQualities, getPhotoOptionCheckedIndex(), getPhotoOptionSelectedListener());
            builder.setTitle(R.string.settings_photo_quality_title);
        }

        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (newQuality > 0 && newQuality != mediaQuality) {
                    mediaQuality = newQuality;
                    dialogInterface.dismiss();
                    cameraControlPanel.lockControls();
                    getCameraController().switchQuality();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        settingsDialog = builder.create();
        settingsDialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(settingsDialog.getWindow().getAttributes());
        layoutParams.width = Utils.convertDipToPixels(this, 350);
        layoutParams.height = Utils.convertDipToPixels(this, 350);
        settingsDialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onCameraTypeChanged(@CameraSwitchView.CameraType int cameraType) {
        if (currentCameraType == cameraType) return;
        currentCameraType = cameraType;

        cameraControlPanel.lockControls();
        cameraControlPanel.allowRecord(false);

        int cameraFace = cameraType == CameraSwitchView.CAMERA_TYPE_FRONT
                ? CamConfiguration.CAMERA_FACE_FRONT : CamConfiguration.CAMERA_FACE_REAR;

        getCameraController().switchCamera(cameraFace);
    }

    @Override
    public void onFlashModeChanged(@FlashSwitchView.FlashMode int mode) {
        switch (mode) {
            case FlashSwitchView.FLASH_AUTO:
                flashMode = CamConfiguration.FLASH_MODE_AUTO;
                getCameraController().setFlashMode(CamConfiguration.FLASH_MODE_AUTO);
                break;
            case FlashSwitchView.FLASH_ON:
                flashMode = CamConfiguration.FLASH_MODE_ON;
                getCameraController().setFlashMode(CamConfiguration.FLASH_MODE_ON);
                break;
            case FlashSwitchView.FLASH_OFF:
                flashMode = CamConfiguration.FLASH_MODE_OFF;
                getCameraController().setFlashMode(CamConfiguration.FLASH_MODE_OFF);
                break;
        }
    }

    @Override
    public void onMediaActionChanged(int mediaActionState) {
        if (currentMediaActionState == mediaActionState) return;
        currentMediaActionState = mediaActionState;
    }

    @Override
    public void onTakePhotoButtonPressed() {
        getCameraController().takePhoto();
    }

    @Override
    public void onStartRecordingButtonPressed() {
        getCameraController().startVideoRecord();
    }

    @Override
    public void onStopRecordingButtonPressed() {
        getCameraController().stopVideoRecord();
    }

    @Override
    protected void onScreenRotation(int degrees) {
        cameraControlPanel.rotateControls(degrees);
        rotateSettingsDialog(degrees);
    }

    @Override
    public int getRequestCode() {
        return requestCode;
    }

    @Override
    public int getMediaAction() {
        return mediaAction;
    }

    @Override
    public int getMediaQuality() {
        return mediaQuality;
    }

    @Override
    public int getVideoDuration() {
        return videoDuration;
    }

    @Override
    public long getVideoFileSize() {
        return videoFileSize;
    }


    @Override
    public int getMinimumVideoDuration() {
        return minimumVideoDuration / 1000;
    }

    @Override
    public int getFlashMode() {
        return flashMode;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateCameraPreview(Size size, View cameraPreview) {
        cameraControlPanel.unLockControls();
        cameraControlPanel.allowRecord(true);

        setCameraPreview(cameraPreview, size);
    }

    @Override
    public void updateUiForMediaAction(@CamConfiguration.MediaAction int mediaAction) {

    }

    @Override
    public void updateCameraSwitcher(int numberOfCameras) {
        cameraControlPanel.allowCameraSwitching(numberOfCameras > 1);
    }

    @Override
    public void onPhotoTaken() {
        startPreviewActivity();
    }

    @Override
    public void onVideoRecordStart(int width, int height) {
        cameraControlPanel.onStartVideoRecord(getCameraController().getOutputFile());
    }

    @Override
    public void onVideoRecordStop() {
        cameraControlPanel.allowRecord(false);
        cameraControlPanel.onStopVideoRecord();
        startPreviewActivity();
    }

    @Override
    public void releaseCameraPreview() {
        clearCameraPreview();
    }

    private void startPreviewActivity() {
        Intent intent = PreviewActivity.newIntent(this,
                getMediaAction(), getCameraController().getOutputFile().toString());
        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PREVIEW_CODE) {
                if (PreviewActivity.isResultConfirm(data)) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(CamConfiguration.Arguments.FILE_PATH,
                            PreviewActivity.getMediaFilePatch(data));
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (PreviewActivity.isResultCancel(data)) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else if (PreviewActivity.isResultRetake(data)) {
                    //ignore, just proceed the camera
                }
            }
        }
    }

    private void rotateSettingsDialog(int degrees) {
        if (settingsDialog != null && settingsDialog.isShowing() && Build.VERSION.SDK_INT > 10) {
            ViewGroup dialogView = (ViewGroup) settingsDialog.getWindow().getDecorView();
            for (int i = 0; i < dialogView.getChildCount(); i++) {
                dialogView.getChildAt(i).setRotation(degrees);
            }
        }
    }

    protected abstract CharSequence[] getVideoQualityOptions();

    protected abstract CharSequence[] getPhotoQualityOptions();

    protected int getVideoOptionCheckedIndex() {
        int checkedIndex = -1;
        if (mediaQuality == CamConfiguration.MEDIA_QUALITY_AUTO) checkedIndex = 0;
        else if (mediaQuality == CamConfiguration.MEDIA_QUALITY_HIGH) checkedIndex = 1;
        else if (mediaQuality == CamConfiguration.MEDIA_QUALITY_MEDIUM) checkedIndex = 2;
        else if (mediaQuality == CamConfiguration.MEDIA_QUALITY_LOW) checkedIndex = 3;

        if (passedMediaQuality != CamConfiguration.MEDIA_QUALITY_AUTO) checkedIndex--;

        return checkedIndex;
    }

    protected int getPhotoOptionCheckedIndex() {
        int checkedIndex = -1;
        if (mediaQuality == CamConfiguration.MEDIA_QUALITY_HIGHEST) checkedIndex = 0;
        else if (mediaQuality == CamConfiguration.MEDIA_QUALITY_HIGH) checkedIndex = 1;
        else if (mediaQuality == CamConfiguration.MEDIA_QUALITY_MEDIUM) checkedIndex = 2;
        else if (mediaQuality == CamConfiguration.MEDIA_QUALITY_LOWEST) checkedIndex = 3;
        return checkedIndex;
    }

    protected DialogInterface.OnClickListener getVideoOptionSelectedListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                newQuality = ((VideoQualityOption) videoQualities[index]).getMediaQuality();
            }
        };
    }

    protected DialogInterface.OnClickListener getPhotoOptionSelectedListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                newQuality = ((PhotoQualityOption) photoQualities[index]).getMediaQuality();
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.push_down_out);
    }
}
