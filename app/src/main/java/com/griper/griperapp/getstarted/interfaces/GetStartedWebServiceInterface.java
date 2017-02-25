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
        Form Url Encoded request - JSON response retrieval
     */

    @FormUrlEncoded
    @POST("signupemail.php")
    Observable<SignUpResponseParser> createProfile(@Field("name") String name, @Field("email") String email, @Field("isfb") String isfb, @Field("pass") String pass,
                                                   @Field("userdp") String userdp);

    @FormUrlEncoded
    @POST("loginemail.php")
    Observable<LoginResponseParser> signIn(@Field("email") String email, @Field("pass") String pass);

}
