package com.example.bitbd.network

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.bitbd.BuildConfig
import com.example.bitbd.constant.*
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.login.LogInActivity
import com.example.bitbd.util.BitBDUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class ApiClient {
    private var retrofit: Retrofit? = null
    private var baseUrl: String = BuildConfig.SERVER_URL



    fun getApiClient(thisContext : Context): Retrofit? {

        if(!BitBDUtil.isNetworkAvailable(thisContext)){
            BitBDUtil.showMessage("Please check your connection", WARNING)
            return null
        }


        if (retrofit == null) {

            val token = BitBDPreferences(thisContext).getAuthToken()
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader(AUTH, BEARER + token)
                    .addHeader(ACCEPT, APPLICATION_JSON)
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .build()
                val response: Response = chain.proceed(newRequest)

                if(response.code == 200){
                    return@Interceptor response
                }
                if (response.code == 401) {
                    MyApplication.appContext?.apply {
                        BitBDPreferences(this).logOut()
                        val intent = Intent(this, LogInActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        this.startActivity(intent)
                    }
                    BitBDUtil.showMessage("Please log in again", WARNING)
                    return@Interceptor response
                }
                if(response.code == 500){
                    BitBDUtil.showMessage("Sorry for the inconvenience", ERROR)
                    return@Interceptor response
                }
                response.close()
                chain.proceed(newRequest)
            }).build()


            val gson = GsonBuilder().setLenient().create()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        return retrofit
    }







}
