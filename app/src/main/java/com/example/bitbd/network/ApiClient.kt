package com.example.bitbd.network

import android.content.Context
import com.example.bitbd.constant.AUTH
import com.example.bitbd.constant.BASE_URL
import com.example.bitbd.constant.TOKEN
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.util.BitBDUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


import java.util.concurrent.TimeUnit


class ApiClient {
    private var retrofit: Retrofit? = null
    private var authRetrofit: Retrofit? = null
    private var baseUrl: String = BASE_URL


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


    fun getAuthApiClient(context: Context): Retrofit? {
        val token = BitBDPreferences(context).getAuthToken()
        if(token.isNullOrEmpty()){
            BitBDUtil.showSessionExpireMessage(context)
            return null
        }

        if (authRetrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY


            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(Interceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader(AUTH, TOKEN + token)
                        .build()
                    chain.proceed(newRequest)
                }).build()


            val gson = GsonBuilder().setLenient().create()


            authRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }

        return authRetrofit

    }

}
