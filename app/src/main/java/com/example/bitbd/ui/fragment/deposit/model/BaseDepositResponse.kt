package com.example.bitbd.ui.fragment.deposit.model
import com.google.gson.annotations.SerializedName


data class BaseDepositResponse(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: List<DepositDataResponse>?,

    @SerializedName("message")
    var message: String?
)



