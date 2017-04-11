package com.griper.griperapp.homescreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.activities.ShowMyLikesActivity;
import com.griper.griperapp.homescreen.activities.ShowMyPostsActivity;
import com.griper.griperapp.homescreen.interfaces.ProfileScreenContract;
import com.griper.griperapp.widgets.AppTextView;
import com.griper.griperapp.widgets.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 05-04-2017
 */

public class ProfileScreenFragment extends Fragment implements ProfileScreenContract.View {

    @Bind(R.id.profileImage)
    RoundedImageView roundedImageView;
    @Bind(R.id.profileName)
    AppTextView profileName;

    public static ProfileScreenFragment newInstance() {
        Bundle args = new Bundle();
        ProfileScreenFragment fragment = new ProfileScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileScreenFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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
        UserProfileData userProfileData = UserProfileData.getUserData();
        if (userProfileData != null) {
            profileName.setText(userProfileData.getName());
            if (userProfileData.getImageUrl() != null) {
                Picasso.with(getActivity())
                        .load(userProfileData.getImageUrl())
                        .into(roundedImageView);
            }
        }
    }

    @OnClick(R.id.layoutPosts)
    public void onClickMyPosts() {
        Intent intent = new Intent(getActivity(), ShowMyPostsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layoutLikes)
    public void onClickMyLikes() {
        Intent intent = new Intent(getActivity(), ShowMyLikesActivity.class);
        startActivity(intent);
    }
}
