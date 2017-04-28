package com.griper.griperapp.homescreen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.griper.griperapp.R;
import com.griper.griperapp.homescreen.models.GripesDataModel;
import com.griper.griperapp.utils.CircleTransform;
import com.griper.griperapp.utils.Utils;
import com.griper.griperapp.widgets.AppTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sarthak on 11-04-2017
 */

public class LikesFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<GripesDataModel> dataModelList;

    public LikesFeedAdapter(Context context, List<GripesDataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_likes_feed_card, parent, false);
        return new LikesFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LikesFeedViewHolder likeHolder = (LikesFeedViewHolder) holder;
        String cloudinaryImageUrl = "";
        int userIconRadius = Utils.convertDpToPixel(context,
                (int) context.getResources().getDimension(R.dimen.dimen_50dp));
        cloudinaryImageUrl = Utils.getCircularCloudinaryImageUrl(
                dataModelList.get(position).getPhotosList().get(0).getImageBaseUrl(), dataModelList.get(position).getPhotosList().get(0).getImagePostFixId(),
                dataModelList.get(position).getPhotosList().get(0).getImagePublicId(), userIconRadius);
        if (!TextUtils.isEmpty(cloudinaryImageUrl)) {
            Picasso.with(context)
                    .load(cloudinaryImageUrl)
                    .transform(new CircleTransform())
                    .into(likeHolder.imageGripeLike);
        } else {
            likeHolder.imageGripeLike.setImageResource(R.drawable.profile_image_placeholder);
        }
        likeHolder.likeAddress.setText(dataModelList.get(position).getLocation());
        likeHolder.likeTitle.setText(dataModelList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (dataModelList != null ? dataModelList.size() : 0);
    }

    public class LikesFeedViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imgGripeLike)
        ImageView imageGripeLike;
        @Bind(R.id.likeTitle)
        AppTextView likeTitle;
        @Bind(R.id.likeLocation)
        AppTextView likeAddress;

        public LikesFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
