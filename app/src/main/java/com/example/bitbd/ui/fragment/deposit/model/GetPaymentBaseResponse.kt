package com.example.bitbd.ui.fragment.deposit.model
import com.google.gson.annotations.SerializedName

data class GetPaymentBaseResponse(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: PaymentInfo?,

    @SerializedName("message")
    var message: String? = null
)

