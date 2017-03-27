package com.griper.griperapp.homescreen.presenters;

import android.content.Context;

import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.GripesNearbyScreenContract;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.models.GripesMetaDataModel;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;
import com.griper.griperapp.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

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
                webServiceInterface.getNearbyGripesViaPage(userProfileData.getEmail(), page, userProfileData.getLastKnownLongitude(), userProfileData.getLastKnownLatitude(), 50)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<GripesNearbyResponseParser>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("getNearbyGripesViaPage Api Success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.i(e.getMessage());
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

    }
}
