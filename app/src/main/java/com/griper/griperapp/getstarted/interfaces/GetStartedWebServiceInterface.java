package com.griper.griperapp.getstarted.interfaces;

import com.griper.griperapp.getstarted.parsers.LoginRequestDataParser;
import com.griper.griperapp.getstarted.parsers.LoginResponseParser;
import com.griper.griperapp.getstarted.parsers.SignUpRequestDataParser;
import com.griper.griperapp.getstarted.parsers.SignUpResponseParser;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Sarthak on 21-02-2017
 */

public interface GetStartedWebServiceInterface {
    /*
        Form Url Encoded request - JSON response retrieval with PHP
     */

    /*
        POST API /signup and /login
     */
    @POST("signup")
    Observable<SignUpResponseParser> createProfile(@Body SignUpRequestDataParser requestDataParser);


    @POST("login")
    Observable<LoginResponseParser> signIn(@Body LoginRequestDataParser loginRequestDataParser);

}
