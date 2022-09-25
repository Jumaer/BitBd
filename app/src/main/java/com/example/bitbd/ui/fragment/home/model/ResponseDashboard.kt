package com.example.bitbd.ui.fragment.home.model

import com.google.gson.annotations.SerializedName


data class ResponseDashboard(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: GetBaseResponseDashBoard?,

    @SerializedName("message")
    var message: String?,
)

