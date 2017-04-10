package com.griper.griperapp.homescreen.presenters;

import android.content.Context;

import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.homescreen.interfaces.ShowGripesDetailContract;
import com.griper.griperapp.homescreen.parsers.GripesNearbyLikeResponseParser;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

import javax.inject.Inject;

import okhttp3.internal.Util;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 09-04-2017
 */

public class ShowGripeDetailPresenter implements ShowGripesDetailContract.Presenter {

    @Inject
    Context context;

    @Inject
    HomeScreenWebServiceInterface webServiceInterface;

    private ShowGripesDetailContract.View gripeView;

    public ShowGripeDetailPresenter(ShowGripesDetailContract.View view) {
        this.gripeView = view;
    }

    @Override
    public void callUpdateGripeLikeApi(String gripeId, int position, int likeCount, boolean isLike) {
        if (Utils.isNetworkAvailable(context)) {
            gripeView.animateLikeButtonPressed(isLike, likeCount);
            gripeView.syncLikeUpdateMainScreen(position, likeCount, isLike);
            UserProfileData userProfileData = UserProfileData.getUserData();
            if (userProfileData != null) {
                webServiceInterface.updateGripeLikes(userProfileData.getEmail(), gripeId, isLike)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<GripesNearbyLikeResponseParser>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("Gripe Like Update Completed!");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e.getMessage());
                            }

                            @Override
                            public void onNext(GripesNearbyLikeResponseParser gripesNearbyLikeResponseParser) {
                                if (gripesNearbyLikeResponseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                                    Timber.i("GripeLikeUpdate Response Success");
                                }
                            }
                        });
            }
        }
    }
}
