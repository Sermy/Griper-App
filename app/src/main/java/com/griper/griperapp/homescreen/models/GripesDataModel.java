package com.griper.griperapp.homescreen.models;

import java.util.DuplicateFormatFlagsException;

/**
 * Created by Sarthak on 25-03-2017
 */

public class GripesDataModel {

    private String gripeId;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String location;
    private String imagePublicId;
    private String imageBaseUrl;
    private String imagePostFixId;
    private Integer likeCount;
    private Integer commentCount;
    private boolean isYesPressed = false;
    private boolean isNoPressed = false;
    private boolean likeIncrement = false;

    public GripesDataModel() {
    }

    public GripesDataModel(String gripeId, String title, String location, String imagePublicId, String imageBaseUrl, String imagePostFixId, int likeCount, int commentCount) {
        this.gripeId = gripeId;
        this.title = title;
        this.location = location;
        this.imagePublicId = imagePublicId;
        this.imageBaseUrl = imageBaseUrl;
        this.imagePostFixId = imagePostFixId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public String getGripeId() {
        return gripeId;
    }

    public void setGripeId(String gripeId) {
        this.gripeId = gripeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    public String getImagePostFixId() {
        return imagePostFixId;
    }

    public void setImagePostFixId(String imagePostFixId) {
        this.imagePostFixId = imagePostFixId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isYesPressed() {
        return isYesPressed;
    }

    public void setYesPressed(boolean yesPressed) {
        isYesPressed = yesPressed;
    }

    public boolean isNoPressed() {
        return isNoPressed;
    }

    public void setNoPressed(boolean noPressed) {
        isNoPressed = noPressed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isLikeIncrement() {
        return likeIncrement;
    }

    public void setLikeIncrement(boolean likeIncrement) {
        this.likeIncrement = likeIncrement;
    }
}
