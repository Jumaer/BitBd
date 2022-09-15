package com.example.bitbd.ui.fragment.deposit.model
import com.google.gson.annotations.SerializedName


data class DepositSubmit(

    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: String?,

    @SerializedName("message")
    var message: String?,
)

