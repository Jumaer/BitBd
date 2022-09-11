package com.example.bitbd.ui.activity.login.model


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LogInResponse(@SerializedName("status")
                         var status: String? ,
                         @SerializedName("user")
                         var user: User? ,
                         @SerializedName("authorisation")
                         var authorisation: Authorization?) : Parcelable

