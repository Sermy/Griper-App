package com.griper.griperapp.homescreen.presenters;

import android.content.Context;

import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.GripesNearbyScreenContract;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.models.GripesMetaDataModel;
import com.griper.griperapp.homescreen.parsers.GripesNearbyLikeResponseParser;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 24-03-2017
 */

public class GripesNearbyScreenPresenter implements GripesNearbyScreenContract.Presenter {

    @Inject
    Context context;

    @Inject
    HomeScreenWebServiceInterface webServiceInterface;

    private GripesNearbyScreenContract.View view;
    private UserProfileData userProfileData;
    private boolean isNewGripePostsLoaded = false;

    public GripesNearbyScreenPresenter(GripesNearbyScreenContract.View view) {
        this.view = view;
        userProfileData = UserProfileData.getUserData();
    }

    @Override
    public void callGetNearbyGripesApi(int page) {
        if (Utils.isNetworkAvailable(context)) {
            if (userProfileData != null) {
                if (page == 0) {
                    view.showProgressBar(true);
                    isNewGripePostsLoaded = false;
                } else {
                    view.showLoadMoreProgressBar(true);
                    view.showProgressBar(false);
                    isNewGripePostsLoaded = true;
                }
                webServiceInterface.getNearbyGripesViaPage(userProfileData.getEmail(), page, UserPreferencesData.getUserPreferencesData().getLastKnownLongitude(),
                        UserPreferencesData.getUserPreferencesData().getLastKnownLatitude(), 50)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<GripesNearbyResponseParser>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("getNearbyGripesViaPage Api Success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.i(e.getMessage());
                                onGetNearbyGripesApiFailure(false, true);
                            }

                            @Override
                            public void onNext(GripesNearbyResponseParser gripesNearbyResponseParser) {
                                if (gripesNearbyResponseParser.getItems().size() == 0) {
                                    onGetNearbyGripesApiFailure(true, false);
                                } else {
                                    onGetNearbyGripesApiSuccess(gripesNearbyResponseParser, isNewGripePostsLoaded);
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onGetNearbyGripesApiSuccess(GripesNearbyResponseParser gripesNearbyResponseParser, boolean isNewGripePostsLoaded) {
        GripesMetaDataModel.setCount(gripesNearbyResponseParser.getMeta().getCount());
        GripesMetaDataModel.setNextPage(gripesNearbyResponseParser.getMeta().getNextPage());
        ArrayList<GripesDataModel> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < gripesNearbyResponseParser.getItems().size(); i++) {
            GripesDataModel dataModel = new GripesDataModel();
            dataModel.setGripeId(gripesNearbyResponseParser.getItems().get(i).getId());
            dataModel.setTitle(gripesNearbyResponseParser.getItems().get(i).getTitle());
            dataModel.setLocation(gripesNearbyResponseParser.getItems().get(i).getAddress());
            dataModel.setImageBaseUrl(gripesNearbyResponseParser.getItems().get(i).getMeta().getPublicHost());
            dataModel.setImagePublicId(gripesNearbyResponseParser.getItems().get(i).getPhoto().getPublicId());
            dataModel.setImagePostFixId(gripesNearbyResponseParser.getItems().get(i).getPhoto().getVersion());
            dataModel.setDescription(gripesNearbyResponseParser.getItems().get(i).getDescription());
            dataModel.setLongitude(gripesNearbyResponseParser.getItems().get(i).getLoc().get(0));
            dataModel.setLatitude(gripesNearbyResponseParser.getItems().get(i).getLoc().get(1));
            dataModel.setLikeCount(gripesNearbyResponseParser.getItems().get(i).getLikeCount());
            dataModel.setCommentCount(gripesNearbyResponseParser.getItems().get(i).getCommentCount());
            dataModel.setYesPressed(gripesNearbyResponseParser.getItems().get(i).getLiked());
            list.add(dataModel);
        }
        if (isNewGripePostsLoaded) {
            view.showNewPosts(list);
        } else {
            view.showPosts(list);
        }
    }

    @Override
    public void onGetNearbyGripesApiFailure(boolean isEmpty, boolean isFailure) {
        view.showLoadMoreProgressBar(isFailure && isEmpty);
    }

    @Override
    public void callUpdateLikesApi(String gripeId, boolean isLiked) {
        if (Utils.isNetworkAvailable(context)) {
            UserProfileData userProfileData = UserProfileData.getUserData();
            if (userProfileData != null) {
                webServiceInterface.updateGripeLikes(userProfileData.getEmail(), gripeId, isLiked)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<GripesNearbyLikeResponseParser>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("Update Gripe Likes Success!");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e("Error: ".concat(e.getMessage()));

                            }

                            @Override
                            public void onNext(GripesNearbyLikeResponseParser gripesNearbyLikeResponseParser) {
                                if (gripesNearbyLikeResponseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                                    Timber.i("LikeUpdate API Response Success");
                                }
                            }
                        });
            }
        } else {
            Utils.showToast(context, context.getString(R.string.string_error_no_network));
        }
    }
}
