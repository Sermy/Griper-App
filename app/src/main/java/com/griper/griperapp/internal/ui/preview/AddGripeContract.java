package com.griper.griperapp.internal.ui.preview;

import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;

/**
 * Created by Sarthak on 07-03-2017
 */

public interface AddGripeContract {

    interface View {
        void init();

        void showProgressBar(boolean show);

        void goToHomeScreen();
    }

    interface Presenter {
        void onAddGripeApiSuccess(AddGripeResponseParser responseParser);

        void onAddGripeApiFailure();

        void callAddGripeApi(String filePath, AddGripeRequestParser requestParser);
    }
}
