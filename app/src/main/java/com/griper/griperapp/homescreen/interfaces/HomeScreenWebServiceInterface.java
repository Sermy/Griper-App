package com.griper.griperapp.homescreen.interfaces;

import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;
import com.griper.griperapp.homescreen.parsers.GripesNearbyLikeResponseParser;
import com.griper.griperapp.homescreen.parsers.GripesNearbyResponseParser;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface.PARAM_PATH_EMAIL;
import static com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface.PARAM_PATH_GRIPE_ID;
import static com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface.PARAM_PATH_PAGE;

/**
 * Created by Sarthak on 15-03-2017
 */

public interface HomeScreenWebServiceInterface {

    String PARAM_PATH_EMAIL = "email";
    String PARAM_PATH_PAGE = "page";
    String PARAM_PATH_GRIPE_ID = "gripeId";
    String QUERY_LATITUDE = "lat";
    String QUERY_LONGITUDE = "lon";
    String QUERY_DISTANCE = "distance";

    @GET("api/{" + PARAM_PATH_EMAIL + "}/getNearbyGripes")
    Observable<List<GripesMapResponseParser>> getNearbyGripes(@Path(PARAM_PATH_EMAIL) String email, @Query(QUERY_LONGITUDE) double lon,
                                                              @Query(QUERY_LATITUDE) double lat, @Query(QUERY_DISTANCE) Integer distance);

    @GET("api/{" + PARAM_PATH_EMAIL + "}/{" + PARAM_PATH_PAGE + "}/getNearbyGripes")
    Observable<GripesNearbyResponseParser> getNearbyGripesViaPage(@Path(PARAM_PATH_EMAIL) String email, @Path(PARAM_PATH_PAGE) int page,
                                                                  @Query(QUERY_LONGITUDE) double lon, @Query(QUERY_LATITUDE) double lat,
                                                                  @Query(QUERY_DISTANCE) Integer distance);

    @PATCH("api/{" + PARAM_PATH_EMAIL + "}/{" + PARAM_PATH_GRIPE_ID + "}/updateGripeLikes")
    Observable<GripesNearbyLikeResponseParser> updateGripeLikes(@Path(PARAM_PATH_EMAIL) String email, @Path(PARAM_PATH_GRIPE_ID) String gripeId);

    @GET("api/{" + PARAM_PATH_EMAIL + "}/{" + PARAM_PATH_PAGE + "}/getMyPosts")
    Observable<GripesNearbyResponseParser> getMyPosts(@Path(PARAM_PATH_EMAIL) String email, @Path(PARAM_PATH_PAGE) int page);

}
