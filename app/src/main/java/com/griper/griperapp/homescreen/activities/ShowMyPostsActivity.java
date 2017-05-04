package com.griper.griperapp.homescreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.homescreen.adapters.GripesFeedAdapter;
import com.griper.griperapp.homescreen.interfaces.ShowMyPostsContract;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.homescreen.models.GripesMetaDataModel;
import com.griper.griperapp.homescreen.models.MyPostsMetaDataModel;
import com.griper.griperapp.homescreen.presenters.ShowMyPostsPresenter;
import com.griper.griperapp.widgets.CustomProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ShowMyPostsActivity extends BaseActivity implements ShowMyPostsContract.View, GripesFeedAdapter.OnFeedItemClickListener {

    @Bind(R.id.progressBarLoadMore)
    protected CustomProgressBar progressBarLoadMore;
    @Bind(R.id.rvFeed)
    protected RecyclerView recyclerViewFeed;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;
    private List<GripesDataModel> myPostsModelList = new ArrayList<>();
    private ShowMyPostsContract.Presenter presenter;
    private GripesFeedAdapter feedAdapter;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_posts);
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
        presenter = new ShowMyPostsPresenter(this);
        getApiComponent().inject((ShowMyPostsPresenter) presenter);
        feedAdapter = new GripesFeedAdapter(this, myPostsModelList, presenter);
//        gripesFeedAdapter.setHasStableIds(true);
        feedAdapter.setOnFeedItemClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewFeed.setLayoutManager(linearLayoutManager);
        recyclerViewFeed.setAdapter(feedAdapter);
        if (!isViewDestroyed()) {
            presenter.callMyPostsApi(0);
        }
        setOnScrollListener();
    }

    @Override
    public boolean isViewDestroyed() {
        return isFinishing();
    }

    @Override
    public void showPosts(List<GripesDataModel> myPostsList) {
        Timber.i("Show My Gripe Posts");
        myPostsModelList.clear();

        for (GripesDataModel gripeModel : myPostsList) {
            myPostsModelList.add(gripeModel);
        }
        showProgressBar(false);
        feedAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showNewPosts(List<GripesDataModel> myLoadMorePostsList) {
        Timber.e("showLoadMoreMyPosts Update UI");
        for (GripesDataModel dataModel : myLoadMorePostsList) {
            myPostsModelList.add(dataModel);
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
                    if (!isLoading && MyPostsMetaDataModel.getCount() != 0 && MyPostsMetaDataModel.getNextPage() != -1) {
                        if ((lastVisibleItemPosition == (totalItemCount - 1)) && !isViewDestroyed()) {
                            isLoading = true;
                            presenter.callMyPostsApi(MyPostsMetaDataModel.getNextPage());
                        }
                    }
                }
            }
        });
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

    @Override
    public void onYesClick(int position, boolean incrementLike, int likeCount) {

    }

    @Override
    public void onCommentsClick(int position) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(CommentsActivity.GRIPE_ID, myPostsModelList.get(position).getGripeId());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
