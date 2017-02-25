package com.griper.griperapp.getstarted.activities;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.getstarted.fragments.EmailLoginFragment;
import com.griper.griperapp.getstarted.interfaces.EmailLoginContract;
import com.griper.griperapp.getstarted.interfaces.EmailProfileContract;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Sarthak on 24-02-2017
 */

/*
    Allows Email SignUp or Email Login to the User.
 */
public class EmailProfileActivity extends BaseActivity implements EmailProfileContract.View {

    private static final String LOGIN_SCREEN = "login_screen";
    private static final String SIGN_UP_SCREEN = "sign_up_screen";

    private EmailLoginContract.View loginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_profile);
        init();
    }

    /*
        Initializing the Activity with Login Fragment
     */
    @Override
    public void init() {
        ButterKnife.bind(this);
        loginView = EmailLoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(0, 0, R.anim.push_up_in, R.anim.push_down_out)
                .replace(R.id.loginFrameLayout, (EmailLoginFragment) loginView)
                .addToBackStack(LOGIN_SCREEN)
                .commit();
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
