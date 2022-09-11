package com.example.bitbd.ui.activity.login.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    @SerializedName("id")
    var id: Int? ,

    @SerializedName("name")
    var name: String? ,

    @SerializedName("slug")
    var slug: String? ,

    @SerializedName("type")
    var type: String? ,

    @SerializedName("email")
    var email: String? ,

    @SerializedName("username")
    var username: String? ,

    @SerializedName("email_verified_at")
    var emailVerifiedAt: String? ,
    @SerializedName("image")
    var image: String? ,

    @SerializedName("designation")
    var designation: String? ,

    @SerializedName("mobile")
    var mobile: String? ,

    @SerializedName("address")
    var address: String? ,

    @SerializedName("affiliate_code")
    var affiliateCode: String? ,

    @SerializedName("mobile_status")
    var mobileStatus: Int? ,

    @SerializedName("affiliate_status")
    var affiliateStatus: Int? ,

    @SerializedName("commission")
    var commission: String? ,

    @SerializedName("balance")
    var balance: String? ,

    @SerializedName("status")
    var status: Int? ,

    @SerializedName("created_at")
    var createdAt: String? ,

    @SerializedName("updated_at")
    var updatedAt: String?

) : Parcelable

