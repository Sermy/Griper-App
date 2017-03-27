package com.griper.griperapp.homescreen.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sarthak on 24-03-2017
 */

public class GripesNearbyResponseParser {

    @SerializedName("items")
    @Expose
    private List<GripesMapResponseParser> items = null;
    @SerializedName("_meta")
    @Expose
    private GripesNearbyMetaResponseParser meta;

    public List<GripesMapResponseParser> getItems() {
        return items;
    }

    public void setItems(List<GripesMapResponseParser> items) {
        this.items = items;
    }

    public GripesNearbyMetaResponseParser getMeta() {
        return meta;
    }

    public void setMeta(GripesNearbyMetaResponseParser meta) {
        this.meta = meta;
    }

}
