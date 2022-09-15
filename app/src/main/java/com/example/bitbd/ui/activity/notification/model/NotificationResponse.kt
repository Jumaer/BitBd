package com.example.bitbd.ui.activity.notification.model
import com.google.gson.annotations.SerializedName


data class NotificationResponse(
    @SerializedName("id")
    var id: Int?,

    @SerializedName("message")
    var message: String?,

    @SerializedName("created_at")
    var createdAt: String?,

    @SerializedName("updated_at")
    var updatedAt: String? = null
)

