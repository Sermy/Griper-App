package com.griper.griperapp.homescreen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.homescreen.adapters.GripesFeedAdapter;
import com.griper.griperapp.homescreen.adapters.LikesFeedAdapter;
import com.griper.griperapp.homescreen.interfaces.ShowMyLikesContract;
import com.griper.griperapp.homescreen.interfaces.ShowMyPostsContract;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.models.MyLikesMetaDataModel;
import com.griper.griperapp.homescreen.models.MyPostsMetaDataModel;
import com.griper.griperapp.homescreen.presenters.ShowMyLikesPresenter;
import com.griper.griperapp.homescreen.presenters.ShowMyPostsPresenter;
import com.griper.griperapp.widgets.CustomProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Sarthak on 11-04-2017
 */

public class ShowMyLikesActivity extends BaseActivity implements ShowMyLikesContract.View {

    @Bind(R.id.progressBarLoadMore)
    protected CustomProgressBar progressBarLoadMore;
    @Bind(R.id.recyclerViewLikes)
    protected RecyclerView recyclerViewFeed;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;
    private List<GripesDataModel> myLikesModelList = new ArrayList<>();
    private ShowMyLikesContract.Presenter presenter;
    private LikesFeedAdapter feedAdapter;
    private boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gripe_my_likes);
        init();
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
        presenter = new ShowMyLikesPresenter(this);
        getApiComponent().inject((ShowMyLikesPresenter) presenter);
        feedAdapter = new LikesFeedAdapter(this, myLikesModelList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewFeed.setLayoutManager(linearLayoutManager);
        recyclerViewFeed.setAdapter(feedAdapter);
        if (!isViewDestroyed()) {
            presenter.callMyLikesApi(0);
        }
        setOnScrollListener();
    }

    private void setOnScrollListener() {
        recyclerViewFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (!isLoading && MyLikesMetaDataModel.getCount() != 0 && MyLikesMetaDataModel.getNextPage() != -1) {
                        if ((lastVisibleItemPosition == (totalItemCount - 1)) && !isViewDestroyed()) {
                            isLoading = true;
                            presenter.callMyLikesApi(MyLikesMetaDataModel.getNextPage());
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean isViewDestroyed() {
        return isFinishing();
    }

    @Override
    public void showLikedPosts(List<GripesDataModel> myLikedPostsList) {
        Timber.i("Show My Like Posts");
        myLikesModelList.clear();

        for (GripesDataModel gripeModel : myLikedPostsList) {
            myLikesModelList.add(gripeModel);
        }
        showProgressBar(false);
        feedAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showMoreLikedPosts(List<GripesDataModel> myLoadMoreLikedPostsList) {
        Timber.e("showLoadMoreMyLikedPosts Update UI");
        for (GripesDataModel dataModel : myLoadMoreLikedPostsList) {
            myLikesModelList.add(dataModel);
        }
        showLoadMoreProgressBar(false);
        feedAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showProgressBar(boolean show) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
