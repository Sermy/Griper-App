package com.griper.griperapp.homescreen.presenters;

import android.content.Context;

import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.homescreen.interfaces.ShowMyLikesContract;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.models.MyLikesMetaDataModel;
import com.griper.griperapp.homescreen.models.MyPostsMetaDataModel;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;
import com.griper.griperapp.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 11-04-2017
 */

public class ShowMyLikesPresenter implements ShowMyLikesContract.Presenter {
    @Inject
    Context context;

    @Inject
    HomeScreenWebServiceInterface webServiceInterface;

    private ShowMyLikesContract.View view;
    private boolean isNewLikedPostsLoaded = false;

    public ShowMyLikesPresenter(ShowMyLikesContract.View view) {
        this.view = view;
    }

    @Override
    public void callMyLikesApi(int page) {
        if (Utils.isNetworkAvailable(context)) {
            UserProfileData userProfileData = UserProfileData.getUserData();
            if (userProfileData != null) {
                if (page == 0) {
                    view.showProgressBar(true);
                    isNewLikedPostsLoaded = false;
                } else {
                    view.showLoadMoreProgressBar(true);
                    view.showProgressBar(false);
                    isNewLikedPostsLoaded = true;
                }
                webServiceInterface.getMyLikes(userProfileData.getEmail(), page)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<GripesNearbyResponseParser>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("getMyPosts Api Success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.i(e.getMessage());
                                onMyLikedPostsFailure();
                            }

                            @Override
                            public void onNext(GripesNearbyResponseParser myPostsResponseParser) {
                                if (myPostsResponseParser.getItems().size() == 0) {
                                    onMyLikedPostsFailure();
                                } else {
                                    onMyLikedPostsApiSuccess(myPostsResponseParser, isNewLikedPostsLoaded);
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onMyLikedPostsApiSuccess(GripesNearbyResponseParser responseParser, boolean isNewLikedPostsLoaded) {
        MyLikesMetaDataModel.setCount(responseParser.getMeta().getCount());
        MyLikesMetaDataModel.setNextPage(responseParser.getMeta().getNextPage());
        ArrayList<GripesDataModel> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < responseParser.getItems().size(); i++) {
            GripesDataModel dataModel = new GripesDataModel();
            dataModel.setGripeId(responseParser.getItems().get(i).getId());
            dataModel.setTitle(responseParser.getItems().get(i).getTitle());
            dataModel.setLocation(responseParser.getItems().get(i).getAddress());
            dataModel.setImageBaseUrl(responseParser.getItems().get(i).getMeta().getPublicHost());
            dataModel.setImagePublicId(responseParser.getItems().get(i).getPhoto().getPublicId());
            dataModel.setImagePostFixId(responseParser.getItems().get(i).getPhoto().getVersion());
            list.add(dataModel);
        }
        if (isNewLikedPostsLoaded) {
            view.showMoreLikedPosts(list);
        } else {
            view.showLikedPosts(list);
        }
    }

    @Override
    public void onMyLikedPostsFailure() {
        view.showLoadMoreProgressBar(false);
    }
}
