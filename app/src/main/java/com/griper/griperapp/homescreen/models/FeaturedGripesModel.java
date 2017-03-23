package com.griper.griperapp.homescreen.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;

/**
 * Created by Sarthak on 17-03-2017
 */

public class FeaturedGripesModel implements Parcelable {

    private String id;
    private String postCode;
    private String address;
    private Double latitude;
    private Double longitude;
    private String createdBy;
    private String desciption;
    private String title;
    private String category;
    private String createdAt;
    private String imageUrl;
    private String baseUrl;
    private String baseUrlPostFix;
    private String imagePublicId;
    private Boolean isFixed;

    protected FeaturedGripesModel(Parcel in) {
        this.id = in.readString();
        this.address = in.readString();
        this.postCode = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.title = in.readString();
        this.desciption = in.readString();
        this.category = in.readString();
        this.createdAt = in.readString();
        this.createdBy = in.readString();
        this.imageUrl = in.readString();
        this.baseUrl = in.readString();
        this.baseUrlPostFix = in.readString();
        this.imagePublicId = in.readString();
        this.isFixed = in.readInt() == 1;
    }

    public FeaturedGripesModel(GripesMapResponseParser responseParser) {
        id = responseParser.getId();
        postCode = responseParser.getPostcode();
        address = responseParser.getAddress();
        longitude = responseParser.getLoc().get(0);
        latitude = responseParser.getLoc().get(1);
        createdBy = responseParser.getCreatedBy();
        createdAt = responseParser.getCreatedAt();
        desciption = responseParser.getDescription();
        title = responseParser.getTitle();
        category = responseParser.getCategory();
        imageUrl = responseParser.getPhoto().getImageUrl();
        baseUrl = responseParser.getMeta().getPublicHost();
        baseUrlPostFix = responseParser.getPhoto().getVersion();
        imagePublicId = responseParser.getPhoto().getPublicId();
        isFixed = responseParser.getIsFixed();
    }

    public static final Creator<FeaturedGripesModel> CREATOR = new Creator<FeaturedGripesModel>() {
        @Override
        public FeaturedGripesModel createFromParcel(Parcel in) {
            return new FeaturedGripesModel(in);
        }

        @Override
        public FeaturedGripesModel[] newArray(int size) {
            return new FeaturedGripesModel[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.address);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeString(this.postCode);
        parcel.writeString(this.title);
        parcel.writeString(this.desciption);
        parcel.writeString(this.createdBy);
        parcel.writeString(this.createdAt);
        parcel.writeString(this.category);
        parcel.writeString(this.imageUrl);
        parcel.writeString(this.baseUrl);
        parcel.writeString(this.baseUrlPostFix);
        parcel.writeString(this.imagePublicId);
        parcel.writeInt(isFixed ? 1 : 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrlPostFix() {
        return baseUrlPostFix;
    }

    public void setBaseUrlPostFix(String baseUrlPostFix) {
        this.baseUrlPostFix = baseUrlPostFix;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public Boolean getFixed() {
        return isFixed;
    }

    public void setFixed(Boolean fixed) {
        isFixed = fixed;
    }
}

