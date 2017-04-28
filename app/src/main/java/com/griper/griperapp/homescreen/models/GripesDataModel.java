package com.griper.griperapp.homescreen.models;

import java.util.DuplicateFormatFlagsException;
import java.util.List;

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
    private List<PhotosDataModel> photosList;
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
        this.photosList = photosList;
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

    public List<PhotosDataModel> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<PhotosDataModel> photosList) {
        this.photosList = photosList;
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
