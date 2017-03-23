package com.griper.griperapp.internal.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.activities.HomeScreenActivity;
import com.griper.griperapp.internal.configuration.CamConfiguration;
import com.griper.griperapp.internal.ui.BaseCamActivity;
import com.griper.griperapp.internal.utils.CamImageLoader;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppButton;
import com.griper.griperapp.widgets.AppEditText;
import com.griper.griperapp.widgets.AppTextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.TransitionManager;

import java.io.File;

import timber.log.Timber;

/**
 * Created by Sarthak on 26-02-2017
 */
public class PreviewActivity extends BaseActivity implements View.OnClickListener, SlidingUpPanelLayout.PanelSlideListener, AddGripeContract.View {

    private static final String TAG = "PreviewActivity";

    private final static String MEDIA_ACTION_ARG = "media_action_arg";
    private final static String FILE_PATH_ARG = "file_path_arg";
    private final static String RESPONSE_CODE_ARG = "response_code_arg";
    private final static String VIDEO_POSITION_ARG = "current_video_position";
    private final static String VIDEO_IS_PLAYED_ARG = "is_played";
    private final static String MIME_TYPE_VIDEO = "video";
    private final static String MIME_TYPE_IMAGE = "image";

    private Integer categoryGripes;

    private int mediaAction;
    private String previewFilePath;

    private FrameLayout photoPreviewContainer;
    private ImageView imagePreview;
    private ImageView imageSlideExpand;

    private MediaController mediaController;
    private MediaPlayer mediaPlayer;

    private int currentPlaybackPosition = 0;
    private boolean isVideoPlaying = true;

    private int currentRatioIndex = 0;

    RelativeLayout griperRoadLayout;
    RelativeLayout griperSprayLayout;
    RelativeLayout griperLitterLayout;
    RelativeLayout griperOtherLayout;
    AppEditText etTitle;
    AppEditText etDescription;
    AppButton buttonGripe;
    AppTextView tvGripeLocation;

    LinearLayout progressBar;
    SlidingUpPanelLayout mLayout;
    ViewGroup layoutDrag;

    private AddGripeContract.Presenter presenter;

    @Override
    public void init() {
        presenter = new AddGripePresenter(this);
        getApiComponent().inject((AddGripePresenter) presenter);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void goToHomeScreen() {
        deleteMediaFile();
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public enum PanelState {
        EXPANDED,
        COLLAPSED,
        ANCHORED,
        HIDDEN,
        DRAGGING
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

        if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {

            TransitionManager.beginDelayedTransition(layoutDrag, new Rotate());
            imageSlideExpand.setRotation(360);

        } else if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {

            com.transitionseverywhere.TransitionManager.beginDelayedTransition(layoutDrag, new Rotate());
            imageSlideExpand.setRotation(180);

        }
    }

    private MediaController.MediaPlayerControl MediaPlayerControlImpl = new MediaController.MediaPlayerControl() {
        @Override
        public void start() {
            mediaPlayer.start();
        }

        @Override
        public void pause() {
            mediaPlayer.pause();
        }

        @Override
        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int pos) {
            mediaPlayer.seekTo(pos);
        }

        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return mediaPlayer.getAudioSessionId();
        }
    };

    public static Intent newIntent(Context context,
                                   @CamConfiguration.MediaAction int mediaAction,
                                   String filePath) {

        return new Intent(context, PreviewActivity.class)
                .putExtra(MEDIA_ACTION_ARG, mediaAction)
                .putExtra(FILE_PATH_ARG, filePath);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        init();
        photoPreviewContainer = (FrameLayout) findViewById(R.id.photo_preview_container);
        View reTakeMedia = findViewById(R.id.re_take_media);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.panelLayout);
        mLayout.addPanelSlideListener(this);

        layoutDrag = (ViewGroup) findViewById(R.id.layoutTopDrag);
        imageSlideExpand = (ImageView) layoutDrag.findViewById(R.id.imageUpLayout);

        etTitle = (AppEditText) findViewById(R.id.gripeTitle);
        etDescription = (AppEditText) findViewById(R.id.gripeDescription);
        progressBar = (LinearLayout) findViewById(R.id.layoutProgressBar);
        griperRoadLayout = (RelativeLayout) findViewById(R.id.griperRoad);
        griperSprayLayout = (RelativeLayout) findViewById(R.id.griperSpray);
        griperLitterLayout = (RelativeLayout) findViewById(R.id.griperLitter);
        griperOtherLayout = (RelativeLayout) findViewById(R.id.griperOther);
        buttonGripe = (AppButton) findViewById(R.id.gripeButton);
        tvGripeLocation = (AppTextView) findViewById(R.id.gripeLocation);

        UserProfileData userProfileData = UserProfileData.getUserData();
        if (userProfileData != null && userProfileData.getLastKnownAddress() != null) {
            tvGripeLocation.setText(userProfileData.getLastKnownAddress());
        } else {
            tvGripeLocation.setVisibility(View.GONE);
        }

        griperOtherLayout.setOnClickListener(this);
        griperSprayLayout.setOnClickListener(this);
        griperLitterLayout.setOnClickListener(this);
        griperRoadLayout.setOnClickListener(this);
        buttonGripe.setOnClickListener(this);


        if (reTakeMedia != null)
            reTakeMedia.setOnClickListener(this);

        Bundle args = getIntent().getExtras();

        mediaAction = args.getInt(MEDIA_ACTION_ARG);
        previewFilePath = args.getString(FILE_PATH_ARG);
        Log.i("PreviewPath", previewFilePath);
        if (mediaAction == CamConfiguration.MEDIA_ACTION_PHOTO) {
            displayImage();
        } else {
            String mimeType = Utils.getMimeType(previewFilePath);
            if (mimeType.contains(MIME_TYPE_IMAGE)) {
                displayImage();
            } else finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaController != null) {
            mediaController.hide();
            mediaController = null;
        }
    }

    private void displayImage() {
        showImagePreview();
    }

    private void showImagePreview() {
        imagePreview = new ImageView(this);
        CamImageLoader.Builder builder = new CamImageLoader.Builder(this);
        builder.load(previewFilePath).build().into(imagePreview);
        photoPreviewContainer.removeAllViews();
        photoPreviewContainer.addView(imagePreview);
    }

    private void saveVideoParams(Bundle outState) {
        if (mediaPlayer != null) {
            outState.putInt(VIDEO_POSITION_ARG, mediaPlayer.getCurrentPosition());
            outState.putBoolean(VIDEO_IS_PLAYED_ARG, mediaPlayer.isPlaying());
        }
    }

    @Override
    public void onClick(View view) {
        Intent resultIntent = new Intent();
        if (view.getId() == R.id.confirm_media_result) {
            resultIntent.putExtra(RESPONSE_CODE_ARG, BaseCamActivity.ACTION_CONFIRM).putExtra(FILE_PATH_ARG, previewFilePath);
        } else if (view.getId() == R.id.re_take_media) {
            onBackPressed();
        } else if (view.getId() == R.id.griperRoad) {
            categoryGripes = 0;
            griperRoadLayout.setSelected(true);
            griperRoadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper_selected));
            griperOtherLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperLitterLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperSprayLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
        } else if (view.getId() == R.id.griperSpray) {
            categoryGripes = 1;
            griperSprayLayout.setSelected(true);
            griperSprayLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper_selected));
            griperOtherLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperLitterLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperRoadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
        } else if (view.getId() == R.id.griperLitter) {
            categoryGripes = 2;
            griperLitterLayout.setSelected(true);
            griperLitterLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper_selected));
            griperOtherLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperRoadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperSprayLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
        } else if (view.getId() == R.id.griperOther) {
            categoryGripes = 3;
            griperOtherLayout.setSelected(true);
            griperOtherLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper_selected));
            griperRoadLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperLitterLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
            griperSprayLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_category_griper));
        } else if (view.getId() == R.id.gripeButton) {
            Timber.i("OnClick button");
            if ((griperRoadLayout.isSelected() || griperSprayLayout.isSelected() || griperLitterLayout.isSelected() ||
                    griperOtherLayout.isSelected()) && !etTitle.getText().toString().isEmpty() && !etDescription.getText().toString().isEmpty()) {
                Timber.i(categoryGripes + " | " + etTitle.getText() + " | " + etDescription.getText().toString());
                UserProfileData userProfileData = UserProfileData.getUserData();

                if (userProfileData != null) {
                    AddGripeRequestParser requestParser = new AddGripeRequestParser(userProfileData.getEmail(), etTitle.getText().toString(),
                            etDescription.getText().toString(), categoryGripes, userProfileData.getLastKnownLatitude(), userProfileData.getLastKnownLongitude(),
                            userProfileData.getLastKnownAddress(), userProfileData.getPostCode());
                    presenter.callAddGripeApi(previewFilePath, requestParser);
                } else {
                    Timber.e("UserProfileData null");
                }
            } else if (etTitle.getText().toString().isEmpty()) {
                Utils.showToast(this, getString(R.string.string_title_not_empty));
            } else if(etDescription.getText().toString().isEmpty()) {
                Utils.showToast(this, getString(R.string.string_description_not_empty));
            } else {
                Utils.showToast(this, getString(R.string.string_category_not_empty));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            deleteMediaFile();
            super.onBackPressed();
            overridePendingTransition(0, R.anim.push_down_out);

        }
    }

    private boolean deleteMediaFile() {
        File mediaFile = new File(previewFilePath);
        return mediaFile.delete();
    }

    public static boolean isResultConfirm(@NonNull Intent resultIntent) {
        return BaseCamActivity.ACTION_CONFIRM == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    public static String getMediaFilePatch(@NonNull Intent resultIntent) {
        return resultIntent.getStringExtra(FILE_PATH_ARG);
    }

    public static boolean isResultRetake(@NonNull Intent resultIntent) {
        return BaseCamActivity.ACTION_RETAKE == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    public static boolean isResultCancel(@NonNull Intent resultIntent) {
        return BaseCamActivity.ACTION_CANCEL == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

}
