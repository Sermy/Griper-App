package com.griper.griperapp.homescreen.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.griper.griperapp.homescreen.models.FeaturedGripesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 15-03-2017
 */

public class GripesMapResponseParser {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("loc")
    @Expose
    private List<Double> loc = null;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("isFixed")
    @Expose
    private Boolean isFixed;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("photo")
    @Expose
    private GripesMapPhotoResponseParser photo;
    @SerializedName("_meta")
    @Expose
    private GripesMapMetaResponseParser meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Double> getLoc() {
        return loc;
    }

    public void setLoc(List<Double> loc) {
        this.loc = loc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public GripesMapPhotoResponseParser getPhoto() {
        return photo;
    }

    public void setPhoto(GripesMapPhotoResponseParser photo) {
        this.photo = photo;
    }

    public GripesMapMetaResponseParser getMeta() {
        return meta;
    }

    public void setMeta(GripesMapMetaResponseParser meta) {
        this.meta = meta;
    }

    public Boolean getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        this.isFixed = isFixed;
    }
}
