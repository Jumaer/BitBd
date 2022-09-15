package com.example.bitbd.ui.activity.main.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

data class LogOutResponse(
    @SerializedName("status")
    var status: String?,

    @SerializedName("message")
    var message: String?
)

