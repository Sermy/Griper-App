package com.griper.griperapp.getstarted.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 22-02-2017
 */

public class LoginResponseParser {

    @SerializedName("user")
    @Expose
    private LoginUserResponseParser user = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public LoginUserResponseParser getUser() {
        return user;
    }

    public void setUser(LoginUserResponseParser user) {
        this.user = user;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
