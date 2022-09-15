package com.example.bitbd.ui.fragment.transaction.model
import com.google.gson.annotations.SerializedName




data class BaseTransactionResponse (
    @SerializedName("success")
    var success: Boolean? ,

    @SerializedName("data")
    var data: List<TransactionObject?>? ,

    @SerializedName("message")
    var message: String? = null

 )