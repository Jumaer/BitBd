package com.example.bitbd.ui.fragment.profile.model

import com.example.bitbd.ui.activity.login.model.User
import com.google.gson.annotations.SerializedName

data class BaseProfileUpdate(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: User?,

    @SerializedName("message")
    var message: String? = null
)
