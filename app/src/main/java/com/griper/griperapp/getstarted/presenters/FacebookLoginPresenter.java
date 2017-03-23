package com.griper.griperapp.getstarted.presenters;

import android.content.Context;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.getstarted.interfaces.FacebookLoginContract;
import com.griper.griperapp.getstarted.interfaces.GetStartedWebServiceInterface;
import com.griper.griperapp.getstarted.parsers.SignUpRequestDataParser;
import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 21-02-2017
 */

public class FacebookLoginPresenter implements FacebookLoginContract.Presenter {

    /*
        Injecting the required stuff
     */
    @Inject
    Context context;

    @Inject
    GetStartedWebServiceInterface getStartedWebServiceInterface;

    private final FacebookLoginContract.View facebookLoginView;
    private final String EXTRA_FB_FIELDS = "fields";
    private final String EXTRA_FB_FIELDS_VALUE = "name,email,gender,age_range,picture";

    public FacebookLoginPresenter(@NonNull FacebookLoginContract.View facebookLoginView) {
        this.facebookLoginView = facebookLoginView;
    }

    @Override
    public void onFacebookLoginApiSuccess(final LoginResult loginResult) {
        // Calling Graph Api
        final GraphRequest request = new GraphRequest(
                loginResult.getAccessToken().getCurrentAccessToken(),
                loginResult.getAccessToken().getCurrentAccessToken().getUserId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() == null) {
                            onGraphApiSuccess(loginResult, response);
                        } else {
                            onGraphApiFailure(response.getError().getErrorMessage());
                        }
                    }
                }
        );
        Timber.i("Graph version" + request.getVersion());
        Bundle requestParameters = new Bundle();
        requestParameters.putString(EXTRA_FB_FIELDS, EXTRA_FB_FIELDS_VALUE);
        request.setParameters(requestParameters);

        if (Utils.isNetworkAvailable(context)) {
            Timber.i("Graph api called");
            facebookLoginView.showProgressBar(true);
            request.executeAsync();
        } else {
            facebookLoginView.showFacebookErrorMessage(context.getString(R.string.string_error_no_network));
        }
    }

    @Override
    public void onFacebookLoginApiFailure(String errorMessage) {

    }

    @Override
    public void onGraphApiSuccess(LoginResult loginResult, GraphResponse response) {
        String responseString = response.getRawResponse();
        Timber.i("Graph Api Response: " + responseString);
        JSONObject responseJSON = response.getJSONObject();
        SignUpRequestDataParser dataParser = null;
        try {
            dataParser = new SignUpRequestDataParser(responseJSON.getString("name"),
                    responseJSON.getString("email"), "", "https://graph.facebook.com/" + responseJSON.getString("id") + "/picture?type=large");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (dataParser != null) {
            callCreateProfileApi(dataParser, loginResult, response);
        } else {
            Timber.e("Unable to parse Graph Api JSON data");
            facebookLoginView.showFacebookErrorMessage(context.getString(R.string.string_error_graph_api_failed));
        }
    }

    @Override
    public void onGraphApiFailure(String errorMessage) {
        Timber.e(errorMessage);
        facebookLoginView.showFacebookErrorMessage(context.getString(R.string.string_error_graph_api_failed));
    }

    @Override
    public void callCreateProfileApi(final SignUpRequestDataParser signUpRequestDataParser, LoginResult loginResult, GraphResponse graphResponse) {
        Timber.i("Call create profile api");
        if (Utils.isNetworkAvailable(context)) {
            getStartedWebServiceInterface.createProfile(signUpRequestDataParser).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends SignUpResponseParser>>() {
                        @Override
                        public Observable<? extends SignUpResponseParser> call(Throwable throwable) {
                            return null;
                        }
                    }).subscribe(new Subscriber<SignUpResponseParser>() {
                @Override
                public void onCompleted() {
                    Timber.i("Completed create profile api call");
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e.getMessage());
                    onCreateProfileApiFailure(e.getMessage());
                }

                @Override
                public void onNext(SignUpResponseParser signUpResponseParser) {
                    if (signUpResponseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                        onCreateProfileApiSuccess(signUpResponseParser, signUpRequestDataParser);
                    } else {
                        onCreateProfileApiFailure(signUpResponseParser.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onCreateProfileApiSuccess(SignUpResponseParser responseParser, SignUpRequestDataParser requestDataParser) {
        UserProfileData.saveUserData(responseParser);
        setUserLoggedinStatus(true);
        facebookLoginView.showProgressBar(false);
//        Utils.showToast(context, context.getString(R.string.string_welcome_user).concat(" ").concat(requestDataParser.getName()));
        facebookLoginView.showHomeScreen();
    }

    private void setUserLoggedinStatus(boolean isLoggedIn) {
        UserPreferencesData preferencesData = UserPreferencesData.getUserPreferencesData();
        preferencesData.setUserLoggedIn(isLoggedIn);
        Timber.i("User Logged in: " + isLoggedIn);
    }

    @Override
    public void onCreateProfileApiFailure(String errorMessage) {
        facebookLoginView.showFacebookErrorMessage(context.getString(R.string.string_error_something_went_wrong));
    }
}

