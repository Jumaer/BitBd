package com.example.bitbd.ui.fragment.home.model
import com.google.gson.annotations.SerializedName


data class GetBaseResponseDashBoard(
    @SerializedName("deposit")
    var deposit: Int?,

    @SerializedName("withdraw")
    var withdraw: Int?,

    @SerializedName("balance")
    var balance: String?
)

