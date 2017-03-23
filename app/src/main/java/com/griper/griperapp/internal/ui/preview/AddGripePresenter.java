package com.griper.griperapp.internal.ui.preview;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;
import com.griper.griperapp.injections.modules.ApiModule;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 07-03-2017
 */

public class AddGripePresenter implements AddGripeContract.Presenter {

    /*
        Injecting the required stuff
     */
    @Inject
    Context context;

    @Inject
    AddGripeWebServiceInterface addGripeWebServiceInterface;

    @Inject
    @Named(ApiModule.MULTIPART)
    AddGripeWebServiceInterface multipartWebServiceInterface;

    private final AddGripeContract.View view;

    public AddGripePresenter(AddGripeContract.View view) {
        this.view = view;
    }

    @Override
    public void onAddGripeApiSuccess(AddGripeResponseParser responseParser) {
        view.showProgressBar(false);
        view.goToHomeScreen();
        Utils.showToast(context.getApplicationContext(), responseParser.getMsg());
    }

    @Override
    public void onAddGripeApiFailure() {
        Utils.showToast(context, context.getString(R.string.string_error_something_went_wrong));
        view.showProgressBar(false);
    }

    @Override
    public void callAddGripeApi(String filePath, AddGripeRequestParser requestParser) {
        //Configure Stuff beforing pushing to server
        Uri newUri = Uri.parse(filePath);
        view.showProgressBar(true);
        Timber.i("new Uri: " + newUri.toString());

        Timber.i("new filepath:" + filePath);
        File newFile = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
        RequestBody category = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(requestParser.getCategory()));
        RequestBody postCode = null;
        RequestBody address = null;
        if (requestParser.getPostcode() != null && requestParser.getAddress() != null) {
            postCode = RequestBody.create(MediaType.parse("text/plain"), requestParser.getPostcode());
            address = RequestBody.create(MediaType.parse("text/plain"), requestParser.getAddress());
        }
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), Double.toString(requestParser.getLat()));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), Double.toString(requestParser.getLon()));
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), requestParser.getTitle());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), requestParser.getDescription());
        UserProfileData userProfileData = UserProfileData.getUserData();

        if (userProfileData != null && Utils.isNetworkAvailable(context)) {

            multipartWebServiceInterface.callAddGripe(userProfileData.getEmail(), requestFile, category, title, description, latitude, longitude, address, postCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends AddGripeResponseParser>>() {
                        @Override
                        public Observable<? extends AddGripeResponseParser> call(Throwable throwable) {
                            return null;
                        }
                    }).subscribe(new Subscriber<AddGripeResponseParser>() {
                @Override
                public void onCompleted() {
                    Timber.i("Completed add gripe api call");
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e.getMessage());
                    onAddGripeApiFailure();
                }

                @Override
                public void onNext(AddGripeResponseParser addGripeResponseParser) {
                    if (addGripeResponseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                        onAddGripeApiSuccess(addGripeResponseParser);
                    } else {
                        onAddGripeApiFailure();
                    }
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.string_error_no_network));
        }

    }
}
