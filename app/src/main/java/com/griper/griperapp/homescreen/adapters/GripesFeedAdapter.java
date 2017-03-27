package com.griper.griperapp.homescreen.adapters;

import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.griper.griperapp.R;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;
import com.griper.griperapp.homescreen.interfaces.GripesNearbyScreenContract;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.utils.CloudinaryImageUrl;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppTextView;
import com.griper.griperapp.widgets.SquareImageView;
import com.squareup.picasso.Picasso;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.PUT;
import timber.log.Timber;

/**
 * Created by Sarthak on 25-03-2017
 */

public class GripesFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<GripesDataModel> gripesDataModelList;
    private GripesNearbyScreenContract.Presenter presenter;

    public GripesFeedAdapter(Context context, List<GripesDataModel> dataModelList, GripesNearbyScreenContract.Presenter presenter) {
        this.context = context;
        this.gripesDataModelList = dataModelList;
        this.presenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gripe_feed_card, parent, false);
        return new GripesFeedViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Timber.i("Holder position " + position);
        GripesFeedViewHolder feedViewHolder = (GripesFeedViewHolder) holder;
        if (UserPreferencesData.getUserPreferencesData().getGripeFeedImageWidth() == 0) {
            setImageLoaded(feedViewHolder, position);
        } else {
            int availableWidth = UserPreferencesData.getUserPreferencesData().getGripeFeedImageWidth();
            CloudinaryImageUrl imageUrl = new CloudinaryImageUrl.Builder(
                    gripesDataModelList.get(position).getImageBaseUrl(), gripesDataModelList.get(position).getImagePublicId(),
                    availableWidth, availableWidth, gripesDataModelList.get(position).getImagePostFixId())
                    .cornerRadius(Utils.convertDpToPixel(context,
                            (int) context.getResources().getDimension(R.dimen.dimen_0dp)))
                    .build();
            if (imageUrl != null) {
                String transformedImageUrl = imageUrl.getTransformedImageUrl();
                if (transformedImageUrl != null) {
                    Picasso.with(context)
                            .load(transformedImageUrl)
                            .fit()
                            .into(feedViewHolder.imageCardGripe);
                }
            }
        }
        if (gripesDataModelList.get(position).isYesPressed()) {
            feedViewHolder.btnNo.setVisibility(View.INVISIBLE);
            feedViewHolder.btnYes.setVisibility(View.VISIBLE);
        } else if (gripesDataModelList.get(position).isNoPressed()) {
            feedViewHolder.btnNo.setVisibility(View.VISIBLE);
            feedViewHolder.btnYes.setVisibility(View.INVISIBLE);
        } else {
            feedViewHolder.btnNo.setVisibility(View.VISIBLE);
            feedViewHolder.btnYes.setVisibility(View.VISIBLE);
        }
        feedViewHolder.cardTitle.setText(gripesDataModelList.get(position).getTitle());
        feedViewHolder.gripeCardLocation.setText(gripesDataModelList.get(position).getLocation());
        if (gripesDataModelList.get(position).getCommentCount() != 0) {
            feedViewHolder.commentsCount.setText(gripesDataModelList.get(position).getCommentCount());
        }
        if (gripesDataModelList.get(position).getLikeCount() != 0) {
            feedViewHolder.likesCount.setText(gripesDataModelList.get(position).getLikeCount());
        }
    }

    private void setImageLoaded(final GripesFeedViewHolder viewHolder, final int position) {
        viewHolder.imageCardGripe.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int availableWidth = viewHolder.imageCardGripe.getMeasuredWidth();
                if (availableWidth > 0) {
                    UserPreferencesData.getUserPreferencesData().setGripeFeedImageWidth(availableWidth);
                    UserPreferencesData.getUserPreferencesData().setGripeFeedImageHeight(availableWidth);
                    viewHolder.imageCardGripe.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    CloudinaryImageUrl imageUrl = new CloudinaryImageUrl.Builder(
                            gripesDataModelList.get(position).getImageBaseUrl(), gripesDataModelList.get(position).getImagePublicId(),
                            availableWidth, availableWidth, gripesDataModelList.get(position).getImagePostFixId())
                            .cornerRadius(Utils.convertDpToPixel(context,
                                    (int) context.getResources().getDimension(R.dimen.dimen_0dp)))
                            .build();
                    if (imageUrl != null) {
                        String transformedImageUrl = imageUrl.getTransformedImageUrl();
                        if (transformedImageUrl != null) {
                            Picasso.with(context)
                                    .load(transformedImageUrl)
                                    .fit()
                                    .into(viewHolder.imageCardGripe);
                        }
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (gripesDataModelList != null ? gripesDataModelList.size() : 0);
    }

    public class GripesFeedViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textViewCardTitle)
        AppTextView cardTitle;
        @Bind(R.id.gripeCardLocation)
        AppTextView gripeCardLocation;
        @Bind(R.id.imageCardGripe)
        SquareImageView imageCardGripe;
        AppTextView btnYes;
        AppTextView btnNo;
        @Bind(R.id.likeCount)
        AppTextView likesCount;
        @Bind(R.id.commentCount)
        AppTextView commentsCount;
        ViewGroup transitionContainer;

        boolean visibleNo = true;
        boolean visibleYes = true;
        private Context context;
        TransitionSet set;

        public GripesFeedViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            transitionContainer = (ViewGroup) itemView.findViewById(R.id.transition_container);
            btnNo = (AppTextView) transitionContainer.findViewById(R.id.textBtnNo);
            btnYes = (AppTextView) transitionContainer.findViewById(R.id.textBtnYes);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.textBtnYes)
        public void onClickBtnYes() {
            if (gripesDataModelList.get(getAdapterPosition()).isYesPressed()) {
                gripesDataModelList.get(getAdapterPosition()).setYesPressed(false);
                visibleNo = true;
                gripesDataModelList.get(getAdapterPosition()).setNoPressed(false);
            } else {
                visibleNo = false;
                gripesDataModelList.get(getAdapterPosition()).setYesPressed(true);
                gripesDataModelList.get(getAdapterPosition()).setNoPressed(false);
            }
            TransitionSet set = new TransitionSet()
                    .addTransition(new Scale(0.7f))
                    .addTransition(new Fade())
                    .setInterpolator((visibleNo ? new LinearOutSlowInInterpolator() :
                            new FastOutLinearInInterpolator()));
            TransitionManager.beginDelayedTransition(transitionContainer, set);
            btnNo.setVisibility(visibleNo ? View.VISIBLE : View.INVISIBLE);
        }

        @OnClick(R.id.textBtnNo)
        public void onClickBtnNo() {
            if (gripesDataModelList.get(getAdapterPosition()).isNoPressed()) {
                visibleYes = true;
                gripesDataModelList.get(getAdapterPosition()).setNoPressed(false);
                gripesDataModelList.get(getAdapterPosition()).setYesPressed(false);
            } else {
                visibleYes = false;
                gripesDataModelList.get(getAdapterPosition()).setNoPressed(true);
                gripesDataModelList.get(getAdapterPosition()).setYesPressed(false);
            }
            TransitionSet set = new TransitionSet()
                    .addTransition(new Scale(0.7f))
                    .addTransition(new Fade())
                    .setInterpolator((visibleYes ? new LinearOutSlowInInterpolator() :
                            new FastOutLinearInInterpolator()));
            TransitionManager.beginDelayedTransition(transitionContainer, set);
            btnYes.setVisibility(visibleYes ? View.VISIBLE : View.INVISIBLE);
        }

    }
}
