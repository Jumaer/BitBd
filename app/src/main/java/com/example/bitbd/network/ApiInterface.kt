package com.example.bitbd.network
import com.example.bitbd.ui.activity.login.model.LogInResponse
import com.example.bitbd.ui.activity.login.model.UserLogIn
import com.example.bitbd.ui.activity.main.model.LogOutResponse
import com.example.bitbd.ui.activity.notification.model.NotificationResponse
import com.example.bitbd.ui.activity.notification.model.NotificationsBaseResponse
import com.example.bitbd.ui.fragment.deposit.model.BaseDepositResponse
import com.example.bitbd.ui.fragment.deposit.model.DepositSubmit
import com.example.bitbd.ui.fragment.deposit.model.GetPaymentBaseResponse
import com.example.bitbd.ui.fragment.profile.model.BaseProfileUpdate
import com.example.bitbd.ui.fragment.transaction.model.BaseTransactionResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {


    @POST("api/v1/login")
    suspend fun userLogin(
        @Body userLogin: UserLogIn
    ): Response<LogInResponse>




    @POST("api/v1/logout")
    suspend fun userLogOut(
    ): Response<LogOutResponse>



    @GET("api/v1/deposit")
    suspend fun deposit(
    ): Response<BaseDepositResponse>



    @GET("api/v1/get-payment-info")
    suspend fun getPaymentInfo(
    ): Response<GetPaymentBaseResponse>




    @GET("api/v1/transaction")
    suspend fun getBaseTransaction(
    ): Response<BaseTransactionResponse>


    @Multipart
    @POST("api/v1/deposit/store")
    suspend fun depositDataStore(
        @Part("account") account: RequestBody,
        @Part("method_id") method_id: RequestBody,
        @Part("trx_id") trx_id: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part  part : MultipartBody.Part
    ):Response<DepositSubmit>



    @GET("api/v1/notification")
    suspend fun getAllNotifications(
    ): Response<NotificationsBaseResponse>


    @Multipart
    @POST("api/v1/profile-update/{slug}")
    suspend fun updateProfile(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_conformation") passwordConform: RequestBody,
        @Path("slug") slug: String,
        @Part  part : MultipartBody.Part
    ):Response<BaseProfileUpdate>

    @FormUrlEncoded
    @POST("api/v1/profile-update/{slug}")
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("password_conformation")  passwordConform: String,
        @Path("slug") slug: String,
    ):Response<BaseProfileUpdate>





    @Multipart
    @POST("api/v1/deposit/store")
    suspend fun submitDepositWithImage(
        @Part("account") account: RequestBody,
        @Part("method_id") method_id: RequestBody,
        @Part("trx_id") trx_id: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part  part : MultipartBody.Part
    ):Response<JsonObject>




}