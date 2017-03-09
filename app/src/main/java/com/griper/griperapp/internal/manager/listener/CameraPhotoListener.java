package com.griper.griperapp.internal.manager.listener;

import java.io.File;

/**
 * Created by Sarthak on 26-02-2017
 */
public interface CameraPhotoListener {
    void onPhotoTaken(File photoFile);

    void onPhotoTakeError();
}
