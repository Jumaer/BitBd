package com.example.bitbd.network

import com.example.bitbd.constant.AUTH
import com.example.bitbd.ui.activity.login.model.LogInResponse
import com.example.bitbd.ui.activity.login.model.UserLogIn
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("api/v1/login")
    suspend fun userLogin(
        @Body userLogin: UserLogIn
    ): Response<LogInResponse>


    @POST("api/v1/logout")
    suspend fun userLogOut(
        @Header(AUTH) token: String
    ): Response<JSONObject>


}