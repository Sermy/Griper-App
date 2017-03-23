package com.griper.griperapp.injections.modules;

import com.griper.griperapp.BuildConfig;
import com.griper.griperapp.getstarted.interfaces.GetStartedWebServiceInterface;
import com.griper.griperapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.griper.griperapp.internal.ui.preview.AddGripeWebServiceInterface;
import com.griper.griperapp.utils.BuildSchemeConstants;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.Path;
import timber.log.Timber;

/**
 * Created by Sarthak on 19-02-2017
 */

@Module
public class ApiModule {

    public static final String MULTIPART = "multipart";

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient provideClient(HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .build();

                return chain.proceed(request);
            }
        });
        builder.connectTimeout(120, TimeUnit.SECONDS);
        return builder.build();
    }

    @Provides
    @Singleton
    public
    @Named(MULTIPART)
    OkHttpClient provideMultipartClient(HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .build();
                return chain.proceed(request);
            }
        });
        builder.connectTimeout(120, TimeUnit.SECONDS);
        return builder.build();

    }


    @Provides
    @Singleton
    public Retrofit provideRetrofitBuilder(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildSchemeConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public
    @Named(MULTIPART)
    Retrofit provideMultipartRetrofitBuilder(@Named(MULTIPART) OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildSchemeConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public GetStartedWebServiceInterface provideService(Retrofit retrofit) {
        return retrofit.create(GetStartedWebServiceInterface.class);
    }

    @Provides
    @Singleton
    public HomeScreenWebServiceInterface provideHomeScreenWebService(Retrofit retrofit) {
        return retrofit.create(HomeScreenWebServiceInterface.class);
    }

    @Provides
    @Singleton
    public AddGripeWebServiceInterface provideAddGripeService(Retrofit retrofit) {
        return retrofit.create(AddGripeWebServiceInterface.class);
    }

    @Provides
    @Singleton
    public
    @Named(MULTIPART)
    AddGripeWebServiceInterface provideMultipartAddGripeService(@Named(MULTIPART) Retrofit retrofit) {
        return retrofit.create(AddGripeWebServiceInterface.class);
    }

}
