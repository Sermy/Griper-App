package com.griper.griperapp.homescreen.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 27-03-2017
 */

public class GripesNearbyLikeResponseParser {

    @SerializedName("gripeId")
    @Expose
    private String gripeId;
    @SerializedName("likeCount")
    @Expose
    private int likeCount;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getGripeId() {
        return gripeId;
    }

    public void setGripeId(String gripeId) {
        this.gripeId = gripeId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
