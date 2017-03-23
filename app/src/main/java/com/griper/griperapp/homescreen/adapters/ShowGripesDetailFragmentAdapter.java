package com.griper.griperapp.homescreen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.griper.griperapp.homescreen.fragments.ShowGripesMapDetailsFragment;
import com.griper.griperapp.homescreen.models.FeaturedGripesModel;
import com.griper.griperapp.homescreen.parsers.GripesMapMetaResponseParser;
import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Sarthak on 17-03-2017
 */

public class ShowGripesDetailFragmentAdapter extends FragmentPagerAdapter {

    private List<FeaturedGripesModel> featuredGripesModelList;
    private List<ShowGripesMapDetailsFragment> fragments = new ArrayList<>();

    public ShowGripesDetailFragmentAdapter(FragmentManager fm, List<FeaturedGripesModel> modelList) {
        super(fm);
        this.featuredGripesModelList = modelList;
    }

    @Override
    public Fragment getItem(int position) {
        Timber.i("getItem :" + position);
        fragments.add(position, ShowGripesMapDetailsFragment.newInstance(featuredGripesModelList.get(position)));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return featuredGripesModelList.size();
    }

    public List<ShowGripesMapDetailsFragment> getFragments() {
        return fragments;
    }
}
