package com.griper.griperapp.homescreen.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.griper.griperapp.R;
import com.griper.griperapp.homescreen.activities.ShowGripeDetailsActivity;
import com.griper.griperapp.homescreen.adapters.ShowGripesDetailFragmentAdapter;
import com.griper.griperapp.homescreen.interfaces.ShowGripesDetailContract;
import com.griper.griperapp.homescreen.models.FeaturedGripesModel;
import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;
import com.griper.griperapp.utils.CloudinaryImageUrl;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.Time;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Sarthak on 17-03-2017
 */

public class ShowGripesMapDetailsFragment extends Fragment implements ShowGripesDetailContract.View {

    private static final String EXTRA_FRAGMENT_GRIPE_DATA = "gripe_data";
    private static final String EXTRA_GRIPE_TITLE = "gripe_title";
    private static final String EXTRA_GRIPE_IMAGE = "gripe_image";
    private static final String EXTRA_GRIPE_ADDRESS = "gripe_address";
    private static final String EXTRA_GRIPE_LAT = "gripe_lat";
    private static final String EXTRA_GRIPE_LON = "gripe_lon";
    private static final String EXTRA_GRIPE_DESCRIPTION = "gripe_description";

    @Bind(R.id.viewHighlight)
    View viewHighlight;
    @Bind(R.id.imageViewGripe)
    ImageView imageViewGripe;
    @Bind(R.id.textViewGripe)
    AppTextView textViewGripe;
    @Bind(R.id.progressCircleGripe)
    AVLoadingIndicatorView indicatorView;

    final int version = Build.VERSION.SDK_INT;
    private FeaturedGripesModel gripesModel;
    private String transformedImageUrl;

    public ShowGripesMapDetailsFragment() {
        // Required empty public constructor
    }

    public static ShowGripesMapDetailsFragment newInstance(FeaturedGripesModel gripesModel) {
        Bundle args = new Bundle();
        ShowGripesMapDetailsFragment fragment = new ShowGripesMapDetailsFragment();
        args.putParcelable(EXTRA_FRAGMENT_GRIPE_DATA, gripesModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_gripe_details, container, false);
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            gripesModel = bundle.getParcelable(EXTRA_FRAGMENT_GRIPE_DATA);
        }
        if (gripesModel != null) {
            showData();
        }
    }

    @Override
    public void showData() {
        imageViewGripe.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int availableHeight = imageViewGripe.getMeasuredHeight();
                int availableWidth = imageViewGripe.getMeasuredWidth();
                if (availableWidth > 0 && availableHeight > 0) {
                    imageViewGripe.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    //save height here
                    CloudinaryImageUrl imageUrl = new CloudinaryImageUrl.Builder(gripesModel.getBaseUrl(), gripesModel.getImagePublicId(),
                            availableWidth, availableHeight, gripesModel.getBaseUrlPostFix())
                            .cornerRadius(Utils.convertDpToPixel(getActivity(), (int) getResources().getDimension(R.dimen.dimen_2dp)))
                            .build();

                    if (imageUrl != null) {
                        transformedImageUrl = imageUrl.getTransformedImageUrl();
//                        Timber.i("transformed url: ".concat(transformedImageUrl));
                        indicatorView.smoothToShow();
                        Picasso.with(getActivity())
                                .load(transformedImageUrl)
                                .into(imageViewGripe, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        indicatorView.smoothToHide();
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                    }
                }
            }
        });
        if (textViewGripe != null) {
            textViewGripe.setText(gripesModel.getTitle());
        }
    }

    @OnClick(R.id.layoutGripe)
    public void onClickLayoutGripe() {
        Intent intent = new Intent(this.getContext(), ShowGripeDetailsActivity.class);
        intent.putExtra(EXTRA_GRIPE_TITLE, gripesModel.getTitle());
        intent.putExtra(EXTRA_GRIPE_ADDRESS, gripesModel.getAddress());
        intent.putExtra(EXTRA_GRIPE_DESCRIPTION, gripesModel.getDesciption());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(gripesModel.getBaseUrl());
        arrayList.add(gripesModel.getImagePublicId());
        arrayList.add(gripesModel.getBaseUrlPostFix());
        intent.putExtra(EXTRA_GRIPE_IMAGE, arrayList);
        intent.putExtra(EXTRA_GRIPE_LAT, gripesModel.getLatitude());
        intent.putExtra(EXTRA_GRIPE_LON, gripesModel.getLongitude());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, 0);
    }

    @Override
    public void setViewHighlighted(boolean show) {
        if (viewHighlight != null && textViewGripe != null) {
            if (show) {
                viewHighlight.setVisibility(View.VISIBLE);
                textViewGripe.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            } else {
                viewHighlight.setVisibility(View.GONE);
                textViewGripe.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_grey_dark));
            }
        }
    }


}
