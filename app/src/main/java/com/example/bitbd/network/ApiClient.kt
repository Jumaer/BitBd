package com.example.bitbd.network

import com.example.bitbd.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


import java.util.concurrent.TimeUnit


class ApiClient {
    private var retrofit: Retrofit? = null
    private var baseUrl: String = BuildConfig.SERVER_URL


    fun getApiClient(): Retrofit? {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

            val gson = GsonBuilder().setLenient().create()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

        }

        return retrofit

    }




}
