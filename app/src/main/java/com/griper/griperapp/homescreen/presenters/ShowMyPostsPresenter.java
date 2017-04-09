package com.griper.griperapp.homescreen.presenters;

import android.content.Context;

import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.homescreen.interfaces.ShowMyPostsContract;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.models.GripesMetaDataModel;
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
 * Created by Sarthak on 05-04-2017
 */

public class ShowMyPostsPresenter implements ShowMyPostsContract.Presenter {

    @Inject
    Context context;

    @Inject
    HomeScreenWebServiceInterface webServiceInterface;

    private ShowMyPostsContract.View view;
    private UserProfileData userProfileData;
    private boolean isNewPostsLoaded = false;

    public ShowMyPostsPresenter(ShowMyPostsContract.View view) {
        this.view = view;
        userProfileData = UserProfileData.getUserData();
    }

    @Override
    public void callMyPostsApi(int page) {
        if (Utils.isNetworkAvailable(context)) {
            if (userProfileData != null) {
                if (page == 0) {
                    view.showProgressBar(true);
                    isNewPostsLoaded = false;
                } else {
                    view.showLoadMoreProgressBar(true);
                    view.showProgressBar(false);
                    isNewPostsLoaded = true;
                }
                webServiceInterface.getMyPosts(userProfileData.getEmail(), page)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<GripesNearbyResponseParser>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("getMyPosts Api Success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.i(e.getMessage());
                                onMyPostsFailure();
                            }

                            @Override
                            public void onNext(GripesNearbyResponseParser myPostsResponseParser) {
                                if (myPostsResponseParser.getItems().size() == 0) {
                                    onMyPostsFailure();
                                } else {
                                    onMyPostsApiSuccess(myPostsResponseParser, isNewPostsLoaded);
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onMyPostsApiSuccess(GripesNearbyResponseParser responseParser, boolean isNewPostsLoaded) {
        MyPostsMetaDataModel.setCount(responseParser.getMeta().getCount());
        MyPostsMetaDataModel.setNextPage(responseParser.getMeta().getNextPage());
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
            dataModel.setDescription(responseParser.getItems().get(i).getDescription());
            dataModel.setLongitude(responseParser.getItems().get(i).getLoc().get(0));
            dataModel.setLatitude(responseParser.getItems().get(i).getLoc().get(1));
            dataModel.setLikeCount(responseParser.getItems().get(i).getLikeCount());
            dataModel.setYesPressed(responseParser.getItems().get(i).getLiked());
            dataModel.setCommentCount(responseParser.getItems().get(i).getCommentCount());
            list.add(dataModel);
        }
        if (isNewPostsLoaded) {
            view.showNewPosts(list);
        } else {
            view.showPosts(list);
        }
    }

    @Override
    public void onMyPostsFailure() {
        view.showLoadMoreProgressBar(false);
    }
}
