package com.griper.griperapp.internal.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.griper.griperapp.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by Sarthak on 26-02-2017
 */
public class FlashSwitchView extends ImageButton {

    @FlashMode
    private int currentMode = FLASH_AUTO;

    private FlashModeSwitchListener switchListener;
    private Drawable flashOnDrawable;
    private Drawable flashOffDrawable;
    private Drawable flashAutoDrawable;

    private int tintColor = Color.WHITE;

    public static final int FLASH_ON = 0;
    public static final int FLASH_OFF = 1;
    public static final int FLASH_AUTO = 2;

    @IntDef({FLASH_ON, FLASH_OFF, FLASH_AUTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlashMode {
    }

    public interface FlashModeSwitchListener {
        void onFlashModeChanged(@FlashMode int mode);
    }

    public FlashSwitchView(@NonNull Context context) {
        this(context, null);
    }

    public FlashSwitchView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        flashOnDrawable = ContextCompat.getDrawable(context, R.drawable.ic_flash_on_white_24dp);
        flashOnDrawable = DrawableCompat.wrap(flashOnDrawable);
        DrawableCompat.setTintList(flashOnDrawable.mutate(), ContextCompat.getColorStateList(context, R.drawable.switch_camera_mode_selector));
        flashOffDrawable = ContextCompat.getDrawable(context, R.drawable.ic_flash_off_white_24dp);
        flashOffDrawable = DrawableCompat.wrap(flashOffDrawable);
        DrawableCompat.setTintList(flashOffDrawable.mutate(), ContextCompat.getColorStateList(context, R.drawable.switch_camera_mode_selector));
        flashAutoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_flash_auto_white_24dp);
        flashAutoDrawable = DrawableCompat.wrap(flashAutoDrawable);
        DrawableCompat.setTintList(flashAutoDrawable.mutate(), ContextCompat.getColorStateList(context, R.drawable.switch_camera_mode_selector));
        init();
    }

    private void init() {
//        setBackgroundResource(R.drawable.circle_frame_background_dark);
        setBackgroundResource(R.drawable.circle_frame_stroke_background);
        setOnClickListener(new FlashButtonClickListener());
        setIcon();
    }

    private void setIcon() {
        if (FLASH_OFF == currentMode) {
            setImageDrawable(flashOffDrawable);
        } else if (FLASH_ON == currentMode) {
            setImageDrawable(flashOnDrawable);
        } else setImageDrawable(flashAutoDrawable);

    }

    private void setIconsTint(@ColorInt int tintColor) {
        this.tintColor = tintColor;
        flashOnDrawable.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
        flashOffDrawable.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
        flashAutoDrawable.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
    }

    public void setFlashMode(@FlashMode int mode) {
        this.currentMode = mode;
        setIcon();
    }

    @FlashMode
    public int getCurrentFlasMode() {
        return currentMode;
    }

    public void setFlashSwitchListener(@NonNull FlashModeSwitchListener switchListener) {
        this.switchListener = switchListener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (Build.VERSION.SDK_INT > 10) {
            if (enabled) {
                setAlpha(1f);
            } else {
                setAlpha(0.5f);
            }
        }
    }

    private class FlashButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (FLASH_AUTO == currentMode) {
                currentMode = FLASH_OFF;
            } else if (FLASH_OFF == currentMode) {
                currentMode = FLASH_ON;
            } else if (FLASH_ON == currentMode) {
                currentMode = FLASH_AUTO;
            }
            setIcon();
            if (switchListener != null) {
                switchListener.onFlashModeChanged(currentMode);
            }
        }
    }
}
