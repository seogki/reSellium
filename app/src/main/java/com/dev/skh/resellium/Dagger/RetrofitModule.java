package com.dev.skh.resellium.Dagger;

import com.dev.skh.resellium.BuildConfig;
import com.dev.skh.resellium.Network.ApiInterface;
import com.dev.skh.resellium.Util.Const;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Seogki on 2018. 11. 8..
 */

@Module
public class RetrofitModule {

    @Provides
    @Singleton
    ApiInterface getApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }


    @Provides
    @Singleton
    Retrofit getRetroft(OkHttpClient client) {
        return new Retrofit
                .Builder()
                .baseUrl(Const.server_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient getClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor getHttpLoggingIntercepter() {
        if (BuildConfig.DEBUG) {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

}
