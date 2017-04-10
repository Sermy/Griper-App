package com.griper.griperapp.homescreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.getstarted.activities.FacebookLoginActivity;
import com.griper.griperapp.homescreen.adapters.FeedItemAnimator;
import com.griper.griperapp.homescreen.adapters.GripesFeedAdapter;
import com.griper.griperapp.homescreen.interfaces.GripesNearbyScreenContract;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.models.GripesMetaDataModel;
import com.griper.griperapp.homescreen.presenters.GripesNearbyScreenPresenter;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.CustomProgressBar;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Sarthak on 17-03-2017
 */

public class GripesNearbyScreenFragment extends Fragment implements GripesNearbyScreenContract.View, GripesFeedAdapter.OnFeedItemClickListener {

    @Bind(R.id.progressBarLoadMore)
    protected CustomProgressBar progressBarLoadMore;
    @Bind(R.id.recyclerViewGripes)
    protected RecyclerView recyclerViewGripes;
    @Bind(R.id.indicatorCircle)
    AVLoadingIndicatorView progressBar;
    private GripesFeedAdapter gripesFeedAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<GripesDataModel> gripesModelList = new ArrayList<>();
    private boolean isLoading = false;

    private UpdateItemListener likesListener;
    private GripesNearbyScreenContract.Presenter gripesNearbyPresenter;

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
        gripesNearbyPresenter = new GripesNearbyScreenPresenter(this);
        ((BaseActivity) getActivity()).getApiComponent().inject((GripesNearbyScreenPresenter) gripesNearbyPresenter);

        gripesFeedAdapter = new GripesFeedAdapter(getActivity(), gripesModelList, gripesNearbyPresenter);
//        gripesFeedAdapter.setHasStableIds(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewGripes.setLayoutManager(linearLayoutManager);
        gripesFeedAdapter.setOnFeedItemClickListener(this);
        recyclerViewGripes.setAdapter(gripesFeedAdapter);
        recyclerViewGripes.setItemAnimator(new FeedItemAnimator());
        if (!isViewDestroyed()) {
            gripesNearbyPresenter.callGetNearbyGripesApi(0);
        }
        setOnScrollListener();
    }

    private void setOnScrollListener() {
        recyclerViewGripes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && GripesMetaDataModel.getCount() != 0 && GripesMetaDataModel.getNextPage() != -1) {
                        if ((lastVisibleItemPosition == (totalItemCount - 1)) && !isViewDestroyed()) {
                            isLoading = true;
                            gripesNearbyPresenter.callGetNearbyGripesApi(GripesMetaDataModel.getNextPage());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void showPosts(List<GripesDataModel> gripesList) {
        Timber.i("Show Gripe Feed Posts");
        gripesModelList.clear();

        for (GripesDataModel gripeModel : gripesList) {
            gripesModelList.add(gripeModel);
        }
        showProgressBar(false);
        gripesFeedAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showNewPosts(List<GripesDataModel> list) {
        Timber.e("showLoadMoreGripes Update UI");
        for (GripesDataModel dataModel : list) {
            gripesModelList.add(dataModel);
        }
        showLoadMoreProgressBar(false);
        gripesFeedAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.smoothToShow();
        } else {
            progressBar.smoothToHide();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadMoreProgressBar(boolean show) {
        if (!isViewDestroyed()) {
            if (show) {
                progressBarLoadMore.setVisibility(View.VISIBLE);
            } else {
                progressBarLoadMore.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void updateGripeAdapterLikes(int position, int likeCount, boolean isLike) {
        if (position < gripesModelList.size()) {
            gripesModelList.get(position).setLikeCount(likeCount);
            gripesModelList.get(position).setYesPressed(isLike);
            gripesModelList.get(position).setNoPressed(false);
            gripesFeedAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public boolean isViewDestroyed() {
        return !isAdded();
    }

    public void goToFacebookLoginScreen() {

        Utils.deleteDbTables();
        Intent intent = new Intent(getActivity(), FacebookLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

    }


    @Override
    public void onYesClick(int position, boolean incrementLikeCount, int likeCount) {
        likesListener.syncLikesUpdate(position, gripesModelList.size(), likeCount, incrementLikeCount);
        gripesNearbyPresenter.callUpdateLikesApi(gripesModelList.get(position).getGripeId(), incrementLikeCount);

    }

    public void setOnItemUpdateListener(UpdateItemListener updateListener) {
        this.likesListener = updateListener;
    }

    public interface UpdateItemListener {
        void syncLikesUpdate(int position, int size, int likeCount, boolean isLiked);
    }
}
