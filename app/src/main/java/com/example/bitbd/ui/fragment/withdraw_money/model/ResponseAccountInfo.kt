package com.example.bitbd.ui.fragment.withdraw_money.model

import com.google.gson.annotations.SerializedName

data class ResponseAccountInfo(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: BaseAccountWithdrawResponse?,

    @SerializedName("message")
    var message: String?
)



