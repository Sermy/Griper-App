package com.griper.griperapp.getstarted.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 04-04-2017
 */

public class FacebookLoginRequestParser {
    @SerializedName("data")
    @Expose
    public FacebookLoginRequestDataParser data;

    public FacebookLoginRequestParser(FacebookLoginRequestDataParser data) {
        this.data = data;
    }

    public FacebookLoginRequestDataParser getData() {
        return data;
    }

    public void setData(FacebookLoginRequestDataParser data) {
        this.data = data;
    }
}
