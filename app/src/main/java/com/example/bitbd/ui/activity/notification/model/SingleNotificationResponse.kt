package com.example.bitbd.ui.activity.notification.model
import com.google.gson.annotations.SerializedName


data class SingleNotificationResponse(

    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: NotificationResponse?,

    @SerializedName("message")
    var message: String?
)


