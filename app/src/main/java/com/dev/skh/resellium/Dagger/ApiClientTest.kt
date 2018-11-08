package com.dev.skh.resellium.Dagger

import com.dev.skh.resellium.BuildConfig
import com.dev.skh.resellium.Network.ApiInterface
import com.dev.skh.resellium.Util.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Seogki on 2018. 6. 26..
 */
open class ApiClientTest @Inject constructor() {


    fun create(): ApiInterface {

        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            //debug
            interceptor.level = HttpLoggingInterceptor.Level.BODY

        } else {
            //release
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        }
        val client = OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit
                .Builder()
                .baseUrl(Const.server_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


        return retrofit.create(ApiInterface::class.java)

    }

}