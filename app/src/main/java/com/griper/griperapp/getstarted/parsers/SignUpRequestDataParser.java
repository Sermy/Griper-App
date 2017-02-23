package com.griper.griperapp.getstarted.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 21-02-2017
 */

public class SignUpRequestDataParser {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("userdp")
    @Expose
    private String userdp;
    @SerializedName("isfb")
    private String isfb;

    public SignUpRequestDataParser(String name, String email, String pass, String userdp, String isfb) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.userdp = userdp;
        this.isfb = isfb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserdp() {
        return userdp;
    }

    public void setUserdp(String userdp) {
        this.userdp = userdp;
    }

    public String getIsfb() {
        return isfb;
    }

    public void setIsfb(String isfb) {
        this.isfb = isfb;
    }
}
