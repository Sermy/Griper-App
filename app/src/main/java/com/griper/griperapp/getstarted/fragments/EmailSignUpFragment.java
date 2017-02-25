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
import com.griper.griperapp.getstarted.interfaces.EmailSignUpContract;
import com.griper.griperapp.getstarted.parsers.SignUpRequestDataParser;
import com.griper.griperapp.getstarted.presenters.EmailSignUpPresenter;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 24-02-2017
 */

public class EmailSignUpFragment extends android.support.v4.app.Fragment implements EmailSignUpContract.View {

    @Bind(R.id.etName)
    AppEditText editTextName;
    @Bind(R.id.etEmail)
    AppEditText editTextEmail;
    @Bind(R.id.etPassword)
    AppEditText editTextPassword;
    @Bind(R.id.parentViewSignUp)
    CoordinatorLayout parentView;
    @Bind(R.id.layoutProgressBar)
    LinearLayout progressBar;

    private EmailSignUpContract.Presenter presenter;

    public EmailSignUpFragment() {
        // Required empty public constructor
    }

    public static EmailSignUpFragment newInstance() {
        Bundle args = new Bundle();
        EmailSignUpFragment signUpFragment = new EmailSignUpFragment();
        return signUpFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /*
        Called after onCreateView
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void init() {
        presenter = new EmailSignUpPresenter(this);
        ((BaseActivity) getActivity()).getApiComponent().inject((EmailSignUpPresenter) presenter);
    }

    @Override
    public CoordinatorLayout getParentView() {
        if (parentView != null) {
            return parentView;
        }
        return null;
    }

    @Override
    public void setSignUpDetailsEnabled(boolean show) {
        editTextName.setEnabled(show);
        editTextEmail.setEnabled(show);
        editTextPassword.setEnabled(show);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    /*
        Go back to the Email Login Screen
     */
    @OnClick(R.id.imageViewAlreadyRegistered)
    public void onClickAlreadyRegistered() {
        getActivity().onBackPressed();
    }

    /*
        Sign up user
     */
    @OnClick(R.id.buttonEmailSignUp)
    public void onClickSignUpButton() {
        if (validateSignUp()) {
            SignUpRequestDataParser requestDataParser = new SignUpRequestDataParser(editTextName.getText().toString(),
                    editTextEmail.getText().toString(), editTextPassword.getText().toString(), "null", "0");
            presenter.callCreateProfileApi(requestDataParser);
        }
    }

    private boolean validateSignUp() {

        if (editTextName.getText().toString().isEmpty() || editTextName.getText().toString().length() < 3) {
            Utils.showSnackBar(parentView, getString(R.string.string_name_length_error));
            return false;
        }

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
