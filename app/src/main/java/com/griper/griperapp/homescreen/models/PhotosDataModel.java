package com.griper.griperapp.homescreen.models;

/**
 * Created by Sarthak on 27-04-2017
 */

public class PhotosDataModel {

    private String imagePublicId;
    private String imageBaseUrl;
    private String imagePostFixId;

    public PhotosDataModel() {
    }

    public PhotosDataModel(String imagePublicId, String imageBaseUrl, String imagePostFixId) {
        this.imagePublicId = imagePublicId;
        this.imageBaseUrl = imageBaseUrl;
        this.imagePostFixId = imagePostFixId;
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
}
