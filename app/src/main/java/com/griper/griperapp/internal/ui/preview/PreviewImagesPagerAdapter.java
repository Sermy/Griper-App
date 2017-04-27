package com.griper.griperapp.internal.ui.preview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.griper.griperapp.R;
import com.griper.griperapp.internal.utils.CamImageLoader;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Sarthak on 25-04-2017
 */

public class PreviewImagesPagerAdapter extends PagerAdapter {
    Context context;
    private List<String> imagePaths = new ArrayList<>();

    public PreviewImagesPagerAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.image_preview_layout, container, false);
        ImageView imagePreview = new ImageView(context);
        FrameLayout photoPreviewContainer = (FrameLayout) itemView.findViewById(R.id.photo_preview_container);
        CamImageLoader.Builder builder = new CamImageLoader.Builder(context);
        Timber.e(imagePaths.get(position));
        builder.load(imagePaths.get(position)).build().into(imagePreview);
        photoPreviewContainer.removeAllViews();
        photoPreviewContainer.addView(imagePreview);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }
}
