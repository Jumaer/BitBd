package com.example.bitbd.network

import retrofit2.Retrofit

object RetrofitApiCallCreation {
    private var isInSession = false
    private var apiCall = ApiClient().getApiClient()?.create(ApiInterface::class.java)

    private  var tokenClient : Retrofit? = null
    fun setApiAuthClient (retrofitTokenClient : Retrofit){
        tokenClient = retrofitTokenClient
        setSession(true)
    }

    private var tokenApiCall = tokenClient?.create(ApiInterface::class.java)

    fun setSession(isSession : Boolean){
        isInSession = isSession
    }

    fun getCallingType(): ApiInterface?
    {
        if(isInSession){
            return tokenApiCall
        }
        return apiCall
    }
}