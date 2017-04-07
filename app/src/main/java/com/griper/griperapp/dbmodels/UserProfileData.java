package com.griper.griperapp.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.griper.griperapp.getstarted.parsers.FacebookLoginResponseParser;
import com.griper.griperapp.getstarted.parsers.LoginResponseParser;
import com.griper.griperapp.getstarted.parsers.SignUpRequestDataParser;
import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;

import java.util.List;

/**
 * Created by Sarthak on 20-02-2017
 */
@Table(name = "user_profile_data")
public class UserProfileData extends Model {

    @Column(name = "uId")
    private String uId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    private String gender;
    @Column(name = "age")
    private Integer age;
    @Column(name = "imageUrl")
    private String imageUrl;

    public UserProfileData() {
        super();
    }

    public UserProfileData(String uId, String name, String email) {
        this.uId = uId;
        this.name = name;
        this.email = email;
    }

    public UserProfileData(String uId, String name, String email, String gender, String imageUrl) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    public UserProfileData(String uId, String name, String email, String imageUrl) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static void saveUserData(LoginResponseParser loginResponseParser) {

        if (loginResponseParser != null) {
            deleteUserData();
            UserProfileData userProfileData = new UserProfileData(loginResponseParser.get_id(), loginResponseParser.getName(),
                    loginResponseParser.getEmail());
            userProfileData.save();
        }
    }

    public static void saveUserData(SignUpResponseParser responseParser) {
        if (responseParser != null) {
            deleteUserData();
            UserProfileData userProfileData = new UserProfileData(responseParser.get_id(), responseParser.getName(), responseParser.getEmail());
            userProfileData.save();
        }
    }

    public static void saveUserData(FacebookLoginResponseParser responseParser) {
        if (responseParser != null) {
            deleteUserData();
            UserProfileData userProfileData = new UserProfileData(responseParser.getuId(), responseParser.getName(), responseParser.getEmail(),
                    responseParser.getGender(), responseParser.getImageUrl());
            userProfileData.save();
        }
    }

    public static UserProfileData getUserData() {
        List<UserProfileData> userData = new Select().from(UserProfileData.class).execute();
        if (userData != null && !userData.isEmpty()) {
            return userData.get(0);
        }
        return null;
    }

    public static void deleteUserData() {
        new Delete().from(UserProfileData.class).execute();
    }

    public String getUid() {
        return uId;
    }

    public void setUid(String uid) {
        this.uId = uid;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.save();
    }
}
