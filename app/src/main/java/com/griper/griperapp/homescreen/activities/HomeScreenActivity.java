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
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.homescreen.adapters.ShowHomeScreenAdapter;
import com.griper.griperapp.homescreen.fragments.GripesMapScreenFragment;
import com.griper.griperapp.homescreen.fragments.GripesNearbyScreenFragment;
import com.griper.griperapp.homescreen.fragments.ProfileScreenFragment;
import com.griper.griperapp.homescreen.service.FetchAddressIntentService;
import com.griper.griperapp.internal.Cam;
import com.griper.griperapp.internal.configuration.CamConfiguration;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.getstarted.activities.FacebookLoginActivity;
import com.griper.griperapp.homescreen.interfaces.HomeScreenContract;
import com.griper.griperapp.homescreen.presenters.HomeScreenPresenter;
import com.griper.griperapp.utils.AppConstants;
import com.griper.griperapp.utils.CustomViewPager;
import com.griper.griperapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class HomeScreenActivity extends LocationRequestActivity implements HomeScreenContract.View {

    @Bind(R.id.viewPager)
    protected CustomViewPager viewPager;
    @Bind(R.id.imageViewHome)
    ImageView homeBtn;
    @Bind(R.id.imageViewMap)
    ImageView mapBtn;
    @Bind(R.id.imageViewRecent)
    ImageView recentBtn;
    @Bind(R.id.imageViewProfile)
    ImageView profileBtn;

    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int CAPTURE_MEDIA = 368;

    private AddressResultReceiver mResultReceiver;
    private ShowHomeScreenAdapter homeScreenAdapter;
    private GripesNearbyScreenFragment nearbyScreenFragment;
    private GripesMapScreenFragment mapScreenFragment;
    private ProfileScreenFragment profileScreenFragment;
    private HomeScreenContract.Presenter homeScreenPresenter;
    UserPreferencesData preferencesData;


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

        setupViewPager();
    }

    @Override
    public void findDimensions() {

    }

    @Override
    public void setupViewPager() {
        homeScreenAdapter = new ShowHomeScreenAdapter(getSupportFragmentManager());
        nearbyScreenFragment = GripesNearbyScreenFragment.newInstance();
        mapScreenFragment = GripesMapScreenFragment.newInstance();
        profileScreenFragment = ProfileScreenFragment.newInstance();
        homeScreenAdapter.addFragment(nearbyScreenFragment);
        homeScreenAdapter.addFragment(mapScreenFragment);
        homeScreenAdapter.addFragment(profileScreenFragment);
        viewPager.setAdapter(homeScreenAdapter);
        homeBtn.setImageResource(R.drawable.home_menu_img_selected);
//        viewPager.setPagingEnabled(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                disableHomeNavigationSelected();
                if (position == 0) {
                    homeBtn.setImageResource(R.drawable.home_menu_img_selected);
                } else if (position == 1) {
                    mapBtn.setImageResource(R.drawable.location_menu_img_selected);
//                } else if (position == 2) {
//                    recentBtn.setImageResource(R.drawable.recent_fixed_menu_img_selected);
                } else {
                    profileBtn.setImageResource(R.drawable.profile_menu_img_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    public void disableHomeNavigationSelected() {
        homeBtn.setImageResource(R.drawable.home_menu_img);
        mapBtn.setImageResource(R.drawable.location_menu_img);
        recentBtn.setImageResource(R.drawable.recent_fixed_menu_img);
        profileBtn.setImageResource(R.drawable.profile_menu_img);
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

    @OnClick(R.id.imageViewHome)
    public void onClickHome() {
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.imageViewMap)
    public void onClickMap() {
        viewPager.setCurrentItem(1);
    }

    @OnClick(R.id.imageViewRecent)
    public void onClickRecent() {
//        viewPager.setCurrentItem(2);
    }

    @OnClick(R.id.imageViewProfile)
    public void onClickProfile() {
        viewPager.setCurrentItem(2);        //3
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
        preferencesData = UserPreferencesData.getUserPreferencesData();
        preferencesData.setLastKnownLatitude(latitude);
        preferencesData.setLastKnownLongitude(longitude);
        startIntentService(latitude, longitude);

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
                UserPreferencesData preferencesData = UserPreferencesData.getUserPreferencesData();
                if (preferencesData != null) {
                    if (mAddressOutput != null) {
                        preferencesData.setLastKnownAddress(mAddressOutput);
                        preferencesData.setPostCode(postCode);
                        Timber.i("Address: " + preferencesData.getLastKnownAddress());
                        Timber.i("PostCode: " + preferencesData.getPostCode());
                    } else {
                        Timber.e("Location Address Output null");
                    }
                } else {
                    Timber.e("PreferencesData null");
                }
            }

        }
    }

}
