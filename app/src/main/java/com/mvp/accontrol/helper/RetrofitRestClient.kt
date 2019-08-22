package com.mvp.accontrol.helper

import com.mvp.accontrol.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitRestClient {

    private const val BUILD_CONFIG_MOCK = "mock"

    private const val SERVICE_TIMEOUT = 60L

    private val mRetrofitBuilder by lazy { Retrofit.Builder() }
    private lateinit var mHttpClientBuilder: OkHttpClient.Builder

    fun configureConverterFactory(): RetrofitRestClient {
        mRetrofitBuilder.addConverterFactory(GsonConverterFactory.create())

        return this
    }


    fun configureOkHttp(): RetrofitRestClient {
        mHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(SERVICE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(SERVICE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(SERVICE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG) {
            mHttpClientBuilder.addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }


        mRetrofitBuilder.client(mHttpClientBuilder.build())

        return this
    }

    fun configureBaseUrl(baseUrl: String): RetrofitRestClient {
        mRetrofitBuilder.baseUrl(baseUrl)

        return this
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return mRetrofitBuilder.build().create(serviceClass)
    }
}