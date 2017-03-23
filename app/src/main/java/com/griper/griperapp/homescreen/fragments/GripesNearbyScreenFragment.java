package com.griper.griperapp.homescreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.griper.griperapp.R;
import com.griper.griperapp.getstarted.activities.FacebookLoginActivity;
import com.griper.griperapp.homescreen.interfaces.GripesNearbyScreenContract;
import com.griper.griperapp.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 17-03-2017
 */

public class GripesNearbyScreenFragment extends Fragment implements GripesNearbyScreenContract.View {

    public GripesNearbyScreenFragment() {

    }

    public static GripesNearbyScreenFragment newInstance() {
        Bundle args = new Bundle();
        GripesNearbyScreenFragment fragment = new GripesNearbyScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gripes_nearby_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public void goToFacebookLoginScreen() {

        Utils.deleteDbTables();
        Intent intent = new Intent(getActivity(), FacebookLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

    }

    @OnClick(R.id.etLogOut)
    public void onClickLogOut() {
        goToFacebookLoginScreen();
    }
}
