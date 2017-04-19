package com.griper.griperapp.firebase.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.griper.griperapp.R;
import com.griper.griperapp.firebase.model.CommentDataModel;
import com.griper.griperapp.homescreen.activities.CommentsActivity;
import com.griper.griperapp.utils.CircleTransform;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.Timestamp;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Sarthak on 16-04-2017
 */

public class CommentsAdapter extends FirebaseRecyclerAdapter<CommentDataModel, CommentViewHolder> {

    private Context context;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private LinearLayout view;

    public CommentsAdapter(Context context, LinearLayout view, Class<CommentDataModel> modelClass, int modelLayout, Class<CommentViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this.view = view;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
    }


    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }


    @Override
    protected void populateViewHolder(CommentViewHolder commentViewHolder, CommentDataModel commentDataModel, int position) {
        if (commentDataModel.getName() != null) {
            commentViewHolder.tvName.setText(commentDataModel.getName());
        }
        if (commentDataModel.getText() != null) {
            commentViewHolder.tvComment.setText(commentDataModel.getText());
        }
        if (commentDataModel.getPhotoUrl() != null) {
            Picasso.with(context)
                    .load(commentDataModel.getPhotoUrl())
                    .transform(new CircleTransform())
                    .into(commentViewHolder.ivUserAvatar);
        } else {
            commentViewHolder.ivUserAvatar.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.bg_comment_avatar));
        }
        if (commentDataModel.getPostedAt() != null) {
            commentViewHolder.tvHours.setText(DateUtils.getRelativeTimeSpanString(commentDataModel.getPostedAt()).toString());
        }
    }
}

