package com.griper.griperapp.firebase.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.griper.griperapp.R;
import com.griper.griperapp.widgets.AppTextView;

/**
 * Created by Sarthak on 17-04-2017
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    ImageView ivUserAvatar;
    AppTextView tvComment;
    AppTextView tvName;
    AppTextView tvHours;

    public CommentViewHolder(View view) {
        super(view);
        ivUserAvatar = (ImageView) itemView.findViewById(R.id.ivUserAvatar);
        tvComment = (AppTextView) itemView.findViewById(R.id.tvComment);
        tvName = (AppTextView) itemView.findViewById(R.id.tvName);
        tvHours = (AppTextView) itemView.findViewById(R.id.tvHours);
    }
}
