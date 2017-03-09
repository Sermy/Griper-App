package com.griper.griperapp.homescreen.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import com.griper.griperapp.R;
import com.griper.griperapp.homescreen.service.FetchAddressIntentService;
import com.griper.griperapp.internal.Cam;
import com.griper.griperapp.internal.configuration.CamConfiguration;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.getstarted.activities.FacebookLoginActivity;
import com.griper.griperapp.homescreen.interfaces.HomeScreenContract;
import com.griper.griperapp.homescreen.presenters.HomeScreenPresenter;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class HomeScreenActivity extends LocationRequestActivity implements HomeScreenContract.View {

    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int CAPTURE_MEDIA = 368;

    private AddressResultReceiver mResultReceiver;
    private HomeScreenContract.Presenter homeScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        init();
    }


    @Override
    public void init() {
        ButterKnife.bind(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        homeScreenPresenter = new HomeScreenPresenter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocation();
    }

    @Override
    public void enableCamera() {
        CamConfiguration.Builder builder = new CamConfiguration.Builder(this, CAPTURE_MEDIA);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Utils.showToast(this, getString(R.string.string_accept_permissions));
            return;
        }
        new Cam(builder.build()).launchCamera();
    }

    @Override
    public void goToFacebookLoginScreen() {

        Utils.deleteDbTables();
        Intent intent = new Intent(this, FacebookLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);


    }

    @Override
    public void startIntentService(double latitude, double longitude) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(AppConstants.RECEIVER, mResultReceiver);
        Location location = new Location("location");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        intent.putExtra(AppConstants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    @OnClick(R.id.imageCameraAccess)
    public void onClickCameraAccess() {
        if (Build.VERSION.SDK_INT > 15) {
            askForPermissions(new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSIONS);
        } else {
            enableCamera();
        }

    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        } else {
            enableCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    enableCamera();
                } else {
                    Timber.e("Permissions Denied");
                    Utils.showToast(this, getString(R.string.string_accept_permissions));
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onLocationFailed(String message) {

    }

    @Override
    public void onLocationSuccess(double latitude, double longitude) {
        Timber.i("Location : (" + latitude + "," + longitude + ")");
        UserProfileData userProfileData = UserProfileData.getUserData();
        if (userProfileData != null) {
            UserProfileData.saveUserDataWithLocation(userProfileData.getUid(), userProfileData.getName(), userProfileData.getEmail(), userProfileData.getImageUrl(),
                    latitude, longitude);
            startIntentService(latitude, longitude);
        } else {
            Timber.e("UserProfileData null");
        }
    }

    @OnClick(R.id.etLogOut)
    public void onClickLogOut() {
        goToFacebookLoginScreen();
    }


    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String mAddressOutput = resultData.getString(AppConstants.RESULT_DATA_KEY);
            String postCode = resultData.getString(AppConstants.RESULT_DATA_POSTCODE_KEY);

            if (resultCode == AppConstants.SUCCESS_RESULT) {
                UserProfileData userProfileData = UserProfileData.getUserData();
                if (userProfileData != null) {
                    if (mAddressOutput != null) {
                        userProfileData.setLastKnownAddress(mAddressOutput);
                        userProfileData.setPostCode(postCode);
                        Timber.i("Address: " + userProfileData.getLastKnownAddress());
                        Timber.i("PostCode: " + userProfileData.getPostCode());
                    } else {
                        Timber.e("Location Address Output null");
                    }
                } else {
                    Timber.e("UserProfile null");
                }
            }

        }
    }

}
