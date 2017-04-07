package com.griper.griperapp.getstarted.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 04-04-2017
 */

public class FacebookLoginRequestCredentialsParser {
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("expires_at")
    @Expose
    public long expiresAt;

    public FacebookLoginRequestCredentialsParser(String token, long expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
