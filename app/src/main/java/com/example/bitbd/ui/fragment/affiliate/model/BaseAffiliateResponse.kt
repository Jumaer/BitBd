package com.example.bitbd.ui.fragment.affiliate.model
import com.google.gson.annotations.SerializedName

data class BaseAffiliateResponse (
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: List<AffiliateObject?>?,

    @SerializedName("message")
    var message: String? = null

)