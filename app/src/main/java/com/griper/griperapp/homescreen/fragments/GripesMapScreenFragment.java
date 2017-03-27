package com.griper.griperapp.homescreen.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.adapters.ShowGripesDetailFragmentAdapter;
import com.griper.griperapp.homescreen.interfaces.GripesMapScreenContract;
import com.griper.griperapp.homescreen.models.FeaturedGripesModel;
import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;
import com.griper.griperapp.homescreen.presenters.GripesMapScreenPresenter;
import com.griper.griperapp.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Sarthak on 14-03-2017
 */

public class GripesMapScreenFragment extends Fragment implements GripesMapScreenContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.viewPager)
    protected ViewPager viewPager;
    @Bind(R.id.progressCircle)
    AVLoadingIndicatorView progressView;
    @Bind(R.id.layoutProgressCircle)
    RelativeLayout layoutProgressCircle;

    private Boolean isNight;
    private GoogleMap googleMap;
    private ShowGripesDetailFragmentAdapter viewPagerAdapter;
    private ArrayList<FeaturedGripesModel> gripesModels;
    private ArrayList<Marker> mapMarkers = new ArrayList<>();
    private GripesMapScreenContract.Presenter gripesMapPresenter;

    public GripesMapScreenFragment() {

    }

    public static GripesMapScreenFragment newInstance() {
        Bundle args = new Bundle();
        GripesMapScreenFragment fragment = new GripesMapScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gripes_map_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        init();
    }


    @Override
    public void init() {
        mMapView.onResume();
        MapsInitializer.initialize(this.getActivity().getApplicationContext());
        mMapView.getMapAsync(this);
        gripesMapPresenter = new GripesMapScreenPresenter(this);
        ((BaseActivity) getActivity()).getApiComponent().
                inject((GripesMapScreenPresenter) gripesMapPresenter);
        progressView.smoothToShow();
        gripesMapPresenter.callGetNearbyGripesApi();
    }

    @Override
    public void setupViewPager() {
        viewPager.setPageMargin(Utils.convertDpToPixel(this.getContext(), 20));
        viewPagerAdapter = new ShowGripesDetailFragmentAdapter(getChildFragmentManager(), gripesModels);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        ShowGripesMapDetailsFragment current = viewPagerAdapter.getFragments().get(0);
        current.setViewHighlighted(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Timber.i("Position: " + position);

                if (position > 0 && position < (viewPagerAdapter.getCount() - 1)) {
                    ShowGripesMapDetailsFragment current = viewPagerAdapter.getFragments().get(position);
                    current.setViewHighlighted(true);
                    ShowGripesMapDetailsFragment previous = viewPagerAdapter.getFragments().get(position - 1);
                    previous.setViewHighlighted(false);
                    ShowGripesMapDetailsFragment next = viewPagerAdapter.getFragments().get(position + 1);
                    next.setViewHighlighted(false);
                    updateCameraPositionGripes(position);
                } else if (position == (viewPagerAdapter.getCount() - 1)) {
                    ShowGripesMapDetailsFragment current = viewPagerAdapter.getFragments().get(position);
                    current.setViewHighlighted(true);
                    ShowGripesMapDetailsFragment previous = viewPagerAdapter.getFragments().get(position - 1);
                    previous.setViewHighlighted(false);
                    updateCameraPositionGripes(position);
                } else {
                    ShowGripesMapDetailsFragment current = viewPagerAdapter.getFragments().get(position);
                    current.setViewHighlighted(true);
                    ShowGripesMapDetailsFragment next = viewPagerAdapter.getFragments().get(position + 1);
                    next.setViewHighlighted(false);
                    updateCameraPositionGripes(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setProgressCircleLayoutVisibility(boolean show) {
        if (show) {
            layoutProgressCircle.setVisibility(View.VISIBLE);
        } else {
            progressView.smoothToHide();
            layoutProgressCircle.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateGripesMapMarkers(List<GripesMapResponseParser> responseParserList) {
        gripesModels = gripesMapPresenter.getFeaturesGripesModel(responseParserList);
        for (int i = 0; i < gripesModels.size(); i++) {
            Marker marker = googleMap.addMarker(new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_red)).snippet(String.valueOf(i)).
                    position(new LatLng(gripesModels.get(i).getLatitude(), gripesModels.get(i).getLongitude())));
            mapMarkers.add(marker);
        }
        setProgressCircleLayoutVisibility(false);
        setupViewPager();

    }

    @Override
    public void updateCameraPositionGripes(int position) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gripesModels.get(position).getLatitude(),
                gripesModels.get(position).getLongitude())));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        viewPager.setCurrentItem(Integer.parseInt(marker.getSnippet()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gripesModels.get(Integer.parseInt(marker.getSnippet())).getLatitude(),
                gripesModels.get(Integer.parseInt(marker.getSnippet())).getLongitude())));
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.showToast(getActivity().getApplicationContext(), getString(R.string.string_show_location));
            return;
        }
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMyLocationEnabled(true);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        isNight = hour < 6 || hour > 18;
        /*
        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json_day_retro);
        googleMap.setMapStyle(styleOptions);
        */
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setCompassEnabled(false);
        UserProfileData userProfileData = UserProfileData.getUserData();
        if (gripesModels != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gripesModels.get(0).getLatitude(),
                    gripesModels.get(0).getLongitude())));
        } else if (userProfileData != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userProfileData.getLastKnownLatitude(),
                    userProfileData.getLastKnownLongitude()), 15));
        }

    }

}
