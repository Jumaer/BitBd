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
        if (retrofit == null) {

            val token = BitBDPreferences(thisContext).getAuthToken()
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader(AUTH, BEARER + token)
                    .addHeader(ACCEPT, APPLICATION_JSON)
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .build()
                val response: Response = chain.proceed(newRequest)

                if (response.code == 401) {
                    MyApplication.appContext?.apply {
                        BitBDPreferences(this).logOut()
                        val intent = Intent(this, LogInActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        this.startActivity(intent)
                    }
                    expectedMessage("Please log in again")
                    return@Interceptor response
                }
                if(response.code == 500){
                    expectedMessage("Internal server error")
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


    private fun expectedMessage(message : String){
        val handler = Handler(Looper.getMainLooper())
        handler.post(Runnable {
            Toast.makeText(MyApplication.appContext, message, Toast.LENGTH_LONG).show()
        })
    }




}
