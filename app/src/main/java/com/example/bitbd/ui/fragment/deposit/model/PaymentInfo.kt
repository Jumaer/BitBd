package com.example.bitbd.ui.fragment.deposit.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentInfo(
    @SerializedName("methods")
    var methods: List<PaymentMethod>?,

    @SerializedName("deposit_rate")
    var depositRate: String?,

    @SerializedName("min_deposit")
    var minDeposit: String? = null
) : Parcelable

