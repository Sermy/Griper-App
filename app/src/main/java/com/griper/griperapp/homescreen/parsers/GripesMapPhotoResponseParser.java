package com.griper.griperapp.homescreen.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 15-03-2017
 */

public class GripesMapPhotoResponseParser {

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("public_id")
    @Expose
    private String publicId;
    @SerializedName("version")
    @Expose
    private String version;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
