package com.griper.griperapp.homescreen.presenters;

import android.content.Context;

import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.CommentsScreenContract;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.homescreen.parsers.GripesNearbyLikeResponseParser;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

import javax.inject.Inject;

import retrofit2.http.PUT;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 18-04-2017
 */

public class CommentsScreenPresenter implements CommentsScreenContract.Presenter {

    @Inject
    Context context;

    @Inject
    HomeScreenWebServiceInterface webServiceInterface;

    CommentsScreenContract.View view;

    public CommentsScreenPresenter() {

    }

    public CommentsScreenPresenter(CommentsScreenContract.View view) {
        this.view = view;
    }

    @Override
    public void callUpdateGripeCommentsCount(String gripeId) {
        if (Utils.isNetworkAvailable(context)) {
            UserProfileData userProfileData = UserProfileData.getUserData();
            if (userProfileData != null) {
                webServiceInterface.updateCommentsCount(userProfileData.getEmail(), gripeId)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<GripesNearbyLikeResponseParser>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("UpdateGripeCommentsCount Completed");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e.getMessage());
                            }

                            @Override
                            public void onNext(GripesNearbyLikeResponseParser gripesNearbyLikeResponseParser) {
                                if (gripesNearbyLikeResponseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                                    Timber.i("Update Comments Count Api Success");
                                }
                            }
                        });
            }
        }
    }
}
