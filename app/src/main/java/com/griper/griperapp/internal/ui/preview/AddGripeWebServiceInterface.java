package com.griper.griperapp.internal.ui.preview;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sarthak on 07-03-2017
 */

public interface AddGripeWebServiceInterface {

    String PARAM_PATH_EMAIL = "email";

    @Multipart
    @POST("api/{" + PARAM_PATH_EMAIL + "}/addGripe")
    Observable<AddGripeResponseParser> callAddGripe(@Path(PARAM_PATH_EMAIL) String email, @PartMap() Map<String, RequestBody> files,
                                                    @Part("category") RequestBody category, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("lat") RequestBody latitude,
                                                    @Part("lon") RequestBody longitude, @Part("address") RequestBody address, @Part("postcode") RequestBody postCode);

}

//@Part("photo_file\"; filename=\"pp.jpg") RequestBody file         MultipartBody.Part[] files


