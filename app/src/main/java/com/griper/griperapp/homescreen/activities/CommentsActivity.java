package com.griper.griperapp.homescreen.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.griper.griperapp.BaseActivity;
import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.firebase.adapters.CommentViewHolder;
import com.griper.griperapp.firebase.adapters.CommentsAdapter;
import com.griper.griperapp.firebase.model.CommentDataModel;
import com.griper.griperapp.homescreen.interfaces.CommentsScreenContract;
import com.griper.griperapp.homescreen.models.CommentsUpdateModel;
import com.griper.griperapp.homescreen.presenters.CommentsScreenPresenter;
import com.griper.griperapp.utils.CircleTransform;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppButton;
import com.griper.griperapp.widgets.AppEditText;
import com.griper.griperapp.widgets.AppTextView;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 16-04-2017
 */

public class CommentsActivity extends BaseActivity implements CommentsScreenContract.View {

    @Bind(R.id.contentRoot)
    LinearLayout contentRoot;
    @Bind(R.id.rvComments)
    RecyclerView rvComments;
    @Bind(R.id.llAddComment)
    LinearLayout llAddComment;
    @Bind(R.id.etComment)
    AppEditText etComment;
    @Bind(R.id.btnSendComment)
    AppButton btnSendComment;
    @Bind(R.id.toolbarComments)
    Toolbar toolbar;
    @Bind(R.id.layoutProgressBar)
    LinearLayout progressBar;

    //    private CommentsAdapter commentsAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<CommentDataModel, CommentViewHolder> commentsAdapter;
    private CommentsScreenContract.Presenter presenter;

    public static final String GRIPES_CHILD = "gripes";
    public static final String COMMENTS_CHILD = "comments";
    public static final String GRIPE_ID = "gripe_id";
    public static final int DEFAULT_COMMENTS_LENGTH_LIMIT = 100;
    LinearLayoutManager linearLayoutManager;
    private String gripeId;
    private Context context;
    private static final int DEFAULT_MESSAGE_COUNT = 25;
    private static final String KEY_POSTED_AT = "postedAt";
    private static final String KEY_SENDER_NAME = "name";
    private static final String KEY_UID = "uid";
    private static final String KEY_TEXT = "text";
    private static final String KEY_PHOTO_URL = "photoUrl";

    private boolean onLoadMore = false;
    Query queryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        init();
        if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }


    @Override
    public void init() {
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(false);
            }
        }
        presenter = new CommentsScreenPresenter(this);
        getApiComponent().inject((CommentsScreenPresenter) presenter);
        progressBar.setVisibility(View.VISIBLE);
        gripeId = getIntent().getStringExtra(GRIPE_ID);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvComments.setLayoutManager(linearLayoutManager);
        context = this;
        // Firebase Initializing
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        queryRef = mFirebaseDatabaseReference.child(GRIPES_CHILD).child(gripeId).child(COMMENTS_CHILD).orderByChild(KEY_POSTED_AT);
        commentsAdapter = new CommentsAdapter(this, progressBar, CommentDataModel.class, R.layout.item_comment, CommentViewHolder.class, queryRef);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        commentsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                progressBar.setVisibility(View.GONE);
                int commentMessageCount = commentsAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (commentMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvComments.scrollToPosition(positionStart);
                }
            }

            @Override
            public void onChanged() {
                super.onChanged();
                progressBar.setVisibility(View.GONE);
            }
        });

        rvComments.setAdapter(commentsAdapter);
        etComment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_COMMENTS_LENGTH_LIMIT)});
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    btnSendComment.setEnabled(true);
                } else {
                    btnSendComment.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @OnClick(R.id.btnSendComment)
    public void onClickSendComment() {
        UserProfileData userProfileData = UserProfileData.getUserData();
        if (userProfileData != null) {
            Map<String, Object> message = new HashMap<>();
            message.put(KEY_SENDER_NAME, userProfileData.getName());
            message.put(KEY_UID, userProfileData.getUid());
            message.put(KEY_POSTED_AT, ServerValue.TIMESTAMP);
            message.put(KEY_TEXT, etComment.getText().toString());
            message.put(KEY_PHOTO_URL, userProfileData.getImageUrl());
            mFirebaseDatabaseReference.child(GRIPES_CHILD).child(gripeId).child(COMMENTS_CHILD).push().setValue(message);
            CommentsUpdateModel.setCommentIsPosted(true);
            presenter.callUpdateGripeCommentsCount(gripeId);
            etComment.setText("");
        }
    }

    private void startIntroAnimation() {
        ViewCompat.setElevation(getToolbar(), 0);
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(0);
        llAddComment.setTranslationY(200);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewCompat.setElevation(getToolbar(), Utils.convertDpToPixel(context, 2));
                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {

        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Toolbar getToolbar() {
        return toolbar;
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
