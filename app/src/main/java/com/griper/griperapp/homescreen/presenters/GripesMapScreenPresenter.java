package com.griper.griperapp.homescreen.presenters;

import android.content.Context;

import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.GripesMapScreenContract;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.homescreen.models.FeaturedGripesModel;
import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;
import com.griper.griperapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 20-03-2017
 */

public class GripesMapScreenPresenter implements GripesMapScreenContract.Presenter {

    @Inject
    Context context;

    @Inject
    HomeScreenWebServiceInterface webServiceInterface;

    private GripesMapScreenContract.View view;
    private UserProfileData userProfileData;

    public GripesMapScreenPresenter(GripesMapScreenContract.View view) {
        this.view = view;
        userProfileData = UserProfileData.getUserData();
    }


    @Override
    public void callGetNearbyGripesApi() {
        if (Utils.isNetworkAvailable(context)) {
            if (userProfileData != null) {
                webServiceInterface.getNearbyGripes(userProfileData.getEmail(), userProfileData.getLastKnownLongitude(),
                        userProfileData.getLastKnownLatitude(), 50).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<GripesMapResponseParser>>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("Call Nearby Gripes Api Success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e.getMessage());
                                if (e.getMessage().equals("Index: 0, Size: 0")) {
                                    view.setProgressCircleLayoutVisibility(true);
                                } else {
                                    Utils.showToast(context, context.getString(R.string.string_error_something_went_wrong));
                                }
                            }

                            @Override
                            public void onNext(List<GripesMapResponseParser> responseParserList) {
                                if (responseParserList.size() == 0) {
                                    view.setProgressCircleLayoutVisibility(true);
                                } else {
                                    view.updateGripesMapMarkers(responseParserList);
                                }
                            }
                        });
            }
        }
    }

    @Override
    public ArrayList<FeaturedGripesModel> getFeaturesGripesModel(List<GripesMapResponseParser> responseParserList) {
        ArrayList<FeaturedGripesModel> list = new ArrayList<>();
        if (responseParserList != null && responseParserList.size() != 0) {
            for (int i = 0; i < responseParserList.size(); i++) {
                FeaturedGripesModel gripesModel = new FeaturedGripesModel(responseParserList.get(i));
                list.add(gripesModel);
            }
        }
        return list;
    }
}
