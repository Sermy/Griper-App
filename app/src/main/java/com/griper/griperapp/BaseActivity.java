package com.griper.griperapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.griper.griperapp.injections.components.ApiComponent;
import com.griper.griperapp.injections.modules.ApiModule;

/**
 * Created by Sarthak on 19-02-2017
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ApiComponent getApiComponent() {
        return ((BaseApplication) getApplication()).getApplicationComponent().plusApiComponent(new ApiModule());
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((BaseApplication) getApplication()).updateResumeCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((BaseApplication) getApplication()).updatePauseCount();
    }
}
