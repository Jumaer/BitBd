package com.example.bitbd.ui.activity.notification.model
import com.google.gson.annotations.SerializedName


data class NotificationsBaseResponse(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: List<NotificationResponse?>?,

    @SerializedName("message")
    var message: String? = null
)

