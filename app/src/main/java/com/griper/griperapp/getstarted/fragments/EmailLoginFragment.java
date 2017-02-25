package com.griper.griperapp.getstarted.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.getstarted.interfaces.EmailLoginContract;
import com.griper.griperapp.getstarted.interfaces.EmailSignUpContract;
import com.griper.griperapp.getstarted.parsers.LoginRequestDataParser;
import com.griper.griperapp.getstarted.presenters.EmailLoginPresenter;
import com.griper.griperapp.getstarted.presenters.EmailSignUpPresenter;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppButton;
import com.griper.griperapp.widgets.AppEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 24-02-2017
 */

public class EmailLoginFragment extends android.support.v4.app.Fragment implements EmailLoginContract.View {

    @Bind(R.id.etLoginEmail)
    AppEditText editTextEmail;
    @Bind(R.id.etLoginPassword)
    AppEditText editTextPassword;
    @Bind(R.id.parentViewLogin)
    CoordinatorLayout parentView;
    @Bind(R.id.layoutProgressBar)
    LinearLayout progressBar;


    private EmailSignUpContract.View signUpView;
    private EmailLoginContract.Presenter emailLoginPresenter;
    private static final String SIGN_UP_SCREEN = "sign_up_screen";

    public EmailLoginFragment() {

    }

    public static EmailLoginFragment newInstance() {
        Bundle args = new Bundle();
        EmailLoginFragment fragment = new EmailLoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    /*
        Initializing a Presenter class for this EmailLoginFragment.java
     */
    @Override
    public void init() {
        emailLoginPresenter = new EmailLoginPresenter(this);
        ((BaseActivity) getActivity()).getApiComponent().inject((EmailLoginPresenter) emailLoginPresenter);
    }

    @Override
    public void setLoginDetailsEnabled(boolean show) {
        editTextEmail.setEnabled(show);
        editTextPassword.setEnabled(show);
    }

    @Override
    public CoordinatorLayout getParentView() {
        if (parentView != null) return parentView;
        return null;
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.buttonEmailLogin)
    public void onClickEmailLogin() {
        if (validateLogin()) {
            setLoginDetailsEnabled(false);
            LoginRequestDataParser requestDataParser = new LoginRequestDataParser(editTextEmail.getText().toString(),
                    editTextPassword.getText().toString());
            emailLoginPresenter.callLoginInApi(requestDataParser);
        }
    }

    /*
        Initializing the EmailSignUpFragment with animations, to display the Sign Up Screen
     */
    @OnClick(R.id.imageViewSignUp)
    public void onClickSignUpMove() {
        signUpView = EmailSignUpFragment.newInstance();
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.push_up_in, 0, R.anim.push_down_out, 0)
                .replace(R.id.signUpFrameLayout, (EmailSignUpFragment) signUpView)
                .addToBackStack(SIGN_UP_SCREEN)
                .commit();
    }

    @OnClick(R.id.imageViewBack)
    public void onClickBack() {
        getActivity().finish();
    }

    public boolean validateLogin() {

        if (editTextEmail.getText().toString().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
            Utils.showSnackBar(parentView, getString(R.string.string_email_format_error));
            return false;
        }

        if (editTextPassword.getText().toString().isEmpty() || editTextPassword.getText().toString().length() < 6) {
            Utils.showSnackBar(parentView, getString(R.string.string_password_length_error));
            return false;
        }

        return true;
    }
}
