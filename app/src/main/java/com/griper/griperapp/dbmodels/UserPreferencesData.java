package com.griper.griperapp.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Sarthak on 23-02-2017
 */
@Table(name = "user_preferences_data")
public class UserPreferencesData extends Model {

    @Column(name = "is_user_logged_in")
    private boolean isUserLoggedIn;

    @Column(name = "gripe_large_image_width")
    private int gripeLargeImageWidth;

    @Column(name = "gripe_large_image_height")
    private int gripeLargeImageHeight;

    @Column(name = "gripe_small_image_width")
    private int gripeSmallImageWidth;

    @Column(name = "gripe_small_image_height")
    private int gripeSmallImageHeight;

    @Column(name = "gripe_feed_image_width")
    private int gripeFeedImageWidth;

    @Column(name = "gripe_feed_image_height")
    private int gripeFeedImageHeight;

    public UserPreferencesData() {
        super();
    }

    public static UserPreferencesData getUserPreferencesData() {

        List<UserPreferencesData> userPreferencesData = new Select().from(UserPreferencesData.class).execute();
        if (userPreferencesData != null && !userPreferencesData.isEmpty()) {

            return userPreferencesData.get(0);
        } else {
            return new UserPreferencesData();
        }
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
        this.save();
    }

    public static void deleteUserPreferencesData() {
        new Delete().from(UserPreferencesData.class).execute();
    }

    public Integer getGripeLargeImageWidth() {
        return gripeLargeImageWidth;
    }

    public void setGripeLargeImageWidth(Integer gripeLargeImageWidth) {
        this.gripeLargeImageWidth = gripeLargeImageWidth;
        this.save();
    }

    public Integer getGripeLargeImageHeight() {
        return gripeLargeImageHeight;
    }

    public void setGripeLargeImageHeight(Integer gripeLargeImageHeight) {
        this.gripeLargeImageHeight = gripeLargeImageHeight;
        this.save();
    }

    public Integer getGripeSmallImageWidth() {
        return gripeSmallImageWidth;
    }

    public void setGripeSmallImageWidth(Integer gripeSmallImageWidth) {
        this.gripeSmallImageWidth = gripeSmallImageWidth;
        this.save();
    }

    public Integer getGripeSmallImageHeight() {
        return gripeSmallImageHeight;
    }

    public void setGripeSmallImageHeight(Integer gripeSmallImageHeight) {
        this.gripeSmallImageHeight = gripeSmallImageHeight;
        this.save();
    }

    public int getGripeFeedImageWidth() {
        return gripeFeedImageWidth;
    }

    public void setGripeFeedImageWidth(int gripeFeedImageWidth) {
        this.gripeFeedImageWidth = gripeFeedImageWidth;
        this.save();
    }

    public int getGripeFeedImageHeight() {
        return gripeFeedImageHeight;
    }

    public void setGripeFeedImageHeight(int gripeFeedImageHeight) {
        this.gripeFeedImageHeight = gripeFeedImageHeight;
        this.save();
    }
}
