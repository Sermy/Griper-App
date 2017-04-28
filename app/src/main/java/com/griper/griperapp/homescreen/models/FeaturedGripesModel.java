package com.griper.griperapp.homescreen.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> imageUrlList;
    private String baseUrl;
    private List<String> baseUrlPostFixList;
    private List<String> imagePublicIdList;
    private Boolean isFixed;
    private Boolean isLiked;
    private int likeCount;
    private int commentCount;

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
//        this.imageUrlList = in.createStringArrayList();
        in.readList(imageUrlList, FeaturedGripesModel.class.getClassLoader());
        this.baseUrl = in.readString();
//        this.baseUrlPostFixList = in.createStringArrayList();
//        this.imagePublicIdList = in.createStringArrayList();
        in.readList(baseUrlPostFixList, FeaturedGripesModel.class.getClassLoader());
        in.readList(imagePublicIdList, FeaturedGripesModel.class.getClassLoader());
        this.isFixed = in.readInt() == 1;
        this.isLiked = in.readInt() == 1;
        this.likeCount = in.readInt();
        this.commentCount = in.readInt();
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
        imageUrlList = new ArrayList<>();
        baseUrlPostFixList = new ArrayList<>();
        imagePublicIdList = new ArrayList<>();
        for (int i = 0; i < responseParser.getGripePhotos().size(); i++) {
            imageUrlList.add(responseParser.getGripePhotos().get(i).getImageUrl());
            baseUrlPostFixList.add(responseParser.getGripePhotos().get(i).getVersion());
            imagePublicIdList.add(responseParser.getGripePhotos().get(i).getPublicId());
        }
        baseUrl = responseParser.getMeta().getPublicHost();
        isFixed = responseParser.getIsFixed();
        isLiked = responseParser.getLiked();
        likeCount = responseParser.getLikeCount();
        commentCount = responseParser.getCommentCount();
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
//        parcel.writeStringList(this.imageUrlList);
        parcel.writeList(this.imageUrlList);
        parcel.writeString(this.baseUrl);
//        parcel.writeStringList(this.baseUrlPostFixList);
//        parcel.writeStringList(this.imagePublicIdList);
        parcel.writeList(this.baseUrlPostFixList);
        parcel.writeList(this.imagePublicIdList);
        parcel.writeInt(isFixed ? 1 : 0);
        parcel.writeInt(isLiked ? 1 : 0);
        parcel.writeInt(likeCount);
        parcel.writeInt(commentCount);
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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public List<String> getBaseUrlPostFixList() {
        return baseUrlPostFixList;
    }

    public void setBaseUrlPostFixList(List<String> baseUrlPostFixList) {
        this.baseUrlPostFixList = baseUrlPostFixList;
    }

    public List<String> getImagePublicIdList() {
        return imagePublicIdList;
    }

    public void setImagePublicIdList(List<String> imagePublicIdList) {
        this.imagePublicIdList = imagePublicIdList;
    }

    public Boolean getFixed() {
        return isFixed;
    }

    public void setFixed(Boolean fixed) {
        isFixed = fixed;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}

