package com.griper.griperapp.homescreen.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.griper.griperapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sarthak on 07-04-2017
 */

public class FeedItemAnimator extends DefaultItemAnimator {
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimationsMap = new HashMap<>();
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();

    private int lastAddAnimatedItem = -2;

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    return new FeedItemHolderInfo((String) payload);
                }
            }
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {

        dispatchAddFinished(holder);
        return false;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExists(newHolder);

        if (preInfo instanceof FeedItemHolderInfo) {
            FeedItemHolderInfo feedItemHolderInfo = (FeedItemHolderInfo) preInfo;
            GripesFeedAdapter.GripesFeedViewHolder holder = (GripesFeedAdapter.GripesFeedViewHolder) newHolder;
            updateLikesCounter(holder, holder.getGripesDataModel().getLikeCount(), holder.getGripesDataModel().isLikeIncrement());

        }

        return false;
    }

    private void animateHeartButtton(final GripesFeedAdapter.GripesFeedViewHolder holder) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.likeButton, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.likeButton, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.likeButton.setImageResource(R.drawable.ic_favorite_pink_24dp);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                heartAnimationsMap.remove(holder);
                dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();

        heartAnimationsMap.put(holder, animatorSet);
    }

    private void updateLikesCounter(GripesFeedAdapter.GripesFeedViewHolder holder, int toValue, boolean isIncrement) {
        if (isIncrement) {
            String likesCountTextFrom = holder.likesCounter.getResources().getQuantityString(
                    R.plurals.likes_count, toValue - 1, toValue - 1
            );
            holder.likesCounter.setCurrentText(likesCountTextFrom);
            animateHeartButtton(holder);
        } else {
            String likesCountTextFrom = holder.likesCounter.getResources().getQuantityString(
                    R.plurals.likes_count, toValue + 1, toValue + 1
            );
            holder.likesCounter.setCurrentText(likesCountTextFrom);
            holder.likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        String likesCountTextTo = holder.likesCounter.getResources().getQuantityString(
                R.plurals.likes_count, toValue, toValue
        );
        holder.likesCounter.setText(likesCountTextTo);
    }

    private void cancelCurrentAnimationIfExists(RecyclerView.ViewHolder item) {
        if (likeAnimationsMap.containsKey(item)) {
            likeAnimationsMap.get(item).cancel();
        }
    }

    private void dispatchChangeFinishedIfAllAnimationsEnded(GripesFeedAdapter.GripesFeedViewHolder holder) {
        if (likeAnimationsMap.containsKey(holder)) {
            return;
        }

        dispatchAnimationFinished(holder);
    }


    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelCurrentAnimationIfExists(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        for (AnimatorSet animatorSet : likeAnimationsMap.values()) {
            animatorSet.cancel();
        }
    }

    public static class FeedItemHolderInfo extends ItemHolderInfo {
        public String updateAction;

        public FeedItemHolderInfo(String updateAction) {
            this.updateAction = updateAction;
        }
    }
}
