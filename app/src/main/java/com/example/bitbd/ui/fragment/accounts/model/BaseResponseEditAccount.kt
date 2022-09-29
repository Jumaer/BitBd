package com.example.bitbd.ui.fragment.accounts.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseResponseEditAccount(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("data")
    var data: EditInformationObject?,

    @SerializedName("message")
    var message: String?,
) : Parcelable


