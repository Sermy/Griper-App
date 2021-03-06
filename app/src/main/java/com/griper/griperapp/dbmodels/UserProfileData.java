package com.griper.griperapp.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
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
    @Column(name = "last_known_latitude")
    private double lastKnownLatitude;
    @Column(name = "last_known_longitude")
    private double lastKnownLongitude;
    @Column(name = "last_known_address")
    private String lastKnownAddress;
    @Column(name = "post_code")
    private String postCode;

    public UserProfileData() {
        super();
    }

    public UserProfileData(String uId, String name, String email) {
        this.uId = uId;
        this.name = name;
        this.email = email;
    }

    public UserProfileData(String uId, String name, String email, String gender, Integer age, String imageUrl, double lastKnownLatitude, double lastKnownLongitude) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.imageUrl = imageUrl;
        this.lastKnownLatitude = lastKnownLatitude;
        this.lastKnownLongitude = lastKnownLongitude;
    }

    public UserProfileData(String uId, String name, String email, String imageUrl, double lastKnownLatitude, double lastKnownLongitude) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.lastKnownLatitude = lastKnownLatitude;
        this.lastKnownLongitude = lastKnownLongitude;
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

    public static void saveUserDataWithLocation(String uid, String name, String email, String userDp, double lastKnownLatitude, double lastKnownLongitude) {
        deleteUserData();
        UserProfileData userProfileData = new UserProfileData(uid, name, email, userDp, lastKnownLatitude, lastKnownLongitude);
        userProfileData.save();
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

    public double getLastKnownLatitude() {
        return lastKnownLatitude;
    }

    public void setLastKnownLatitude(double lastKnownLatitude) {
        this.lastKnownLatitude = lastKnownLatitude;
        this.save();
    }

    public double getLastKnownLongitude() {
        return lastKnownLongitude;
    }

    public void setLastKnownLongitude(double lastKnownLongitude) {
        this.lastKnownLongitude = lastKnownLongitude;
        this.save();
    }

    public String getLastKnownAddress() {
        return lastKnownAddress;
    }

    public void setLastKnownAddress(String lastKnownAddress) {
        this.lastKnownAddress = lastKnownAddress;
        this.save();
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
        this.save();
    }
}
