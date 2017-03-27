package com.griper.griperapp.homescreen.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 24-03-2017
 */

public class GripesNearbyMetaResponseParser {

    @SerializedName("next_page")
    @Expose
    private Integer nextPage;
    @SerializedName("count")
    @Expose
    private Integer count;

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
