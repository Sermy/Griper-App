package com.griper.griperapp.internal.ui.preview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 07-03-2017
 */

public class AddGripeResponseParser {

    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("msg")
    @Expose
    private String msg;

    public AddGripeResponseParser() {

    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
