package com.griper.griperapp.homescreen.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.cam.Cam;
import com.griper.griperapp.cam.internal.configuration.CamConfiguration;
import com.griper.griperapp.getstarted.activities.FacebookLoginActivity;
import com.griper.griperapp.homescreen.interfaces.HomeScreenContract;
import com.griper.griperapp.homescreen.presenters.HomeScreenPresenter;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class HomeScreenActivity extends BaseActivity implements HomeScreenContract.View {

    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int CAPTURE_MEDIA = 368;

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
        homeScreenPresenter = new HomeScreenPresenter(this);
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

    @OnClick(R.id.etLogOut)
    public void onClickLogOut() {
        goToFacebookLoginScreen();
    }

}
