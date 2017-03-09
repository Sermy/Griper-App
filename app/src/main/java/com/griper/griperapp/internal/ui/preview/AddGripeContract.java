package com.griper.griperapp.internal.ui.preview;

/**
 * Created by Sarthak on 07-03-2017
 */

public interface AddGripeContract {

    interface View {
        void init();

        void showProgressBar(boolean show);
    }

    interface Presenter {
        void onAddGripeApiSuccess();

        void onAddGripeApiFailure();

        void callAddGripeApi(String filePath, AddGripeRequestParser requestParser);
    }
}
