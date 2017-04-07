package com.griper.griperapp.getstarted.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 04-04-2017
 */

public class FacebookLoginRequestDataParser {
    @SerializedName("facebook_auth_hash")
    @Expose
    public FacebookLoginRequestAuthHashParser facebookAuthHash;

    public FacebookLoginRequestDataParser(FacebookLoginRequestAuthHashParser facebookAuthHash) {
        this.facebookAuthHash = facebookAuthHash;
    }

    public FacebookLoginRequestAuthHashParser getFacebookAuthHash() {
        return facebookAuthHash;
    }

    public void setFacebookAuthHash(FacebookLoginRequestAuthHashParser facebookAuthHash) {
        this.facebookAuthHash = facebookAuthHash;
    }
}
