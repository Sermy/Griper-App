package com.griper.griperapp.homescreen.interfaces;

import com.griper.griperapp.homescreen.parsers.GripesMapResponseParser;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface.PARAM_PATH_EMAIL;

/**
 * Created by Sarthak on 15-03-2017
 */

public interface HomeScreenWebServiceInterface {

    String PARAM_PATH_EMAIL = "email";
    String QUERY_LATITUDE = "lat";
    String QUERY_LONGITUDE = "lon";
    String QUERY_DISTANCE = "distance";

    @GET("api/{" + PARAM_PATH_EMAIL + "}/getNearbyGripes")
    Observable<List<GripesMapResponseParser>> getNearbyGripes(@Path(PARAM_PATH_EMAIL) String email, @Query(QUERY_LONGITUDE) double lon,
                                                              @Query(QUERY_LATITUDE) double lat, @Query(QUERY_DISTANCE) Integer distance);

}
