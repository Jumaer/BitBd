package com.example.bitbd.ui.fragment.deposit.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentMethod(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("branch")
    var branch: String?,

    @SerializedName("account")
    var account: String?,

    @SerializedName("link")
    var link: String?,

    @SerializedName("image")
    var image: String?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("deleted_at")
    var deletedAt: String?,

    @SerializedName("created_at")
    var createdAt: String?,

    @SerializedName("updated_at")
    var updatedAt: String?,
) : Parcelable

