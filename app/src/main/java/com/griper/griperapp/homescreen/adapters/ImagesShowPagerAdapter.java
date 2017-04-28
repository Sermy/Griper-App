package com.griper.griperapp.homescreen.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.griper.griperapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Sarthak on 28-04-2017
 */

public class ImagesShowPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<String> transformedImageUrls = new ArrayList<>();

    public ImagesShowPagerAdapter(Context context, ArrayList<String> transformedImageUrls) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transformedImageUrls.clear();
        this.transformedImageUrls = transformedImageUrls;
    }

    @Override
    public int getCount() {
        return transformedImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.image_gripe_details_layout, container, false);
        ImageView imageViewPhoto = (ImageView) view.findViewById(R.id.imageViewPhoto);
        Timber.i(transformedImageUrls.get(position));
        Picasso.with(mContext)
                .load(transformedImageUrls.get(position))
                .into(imageViewPhoto);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
