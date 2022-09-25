package com.example.bitbd.ui.fragment.withdraw_money.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class BaseResponseWithdraw(
    @SerializedName("id")
    var id: Int?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("trx_name")
  var trxName: String?,

    @SerializedName("trx_account")
    var trxAccount: Any?,

    @SerializedName("trx_id")
    var trxId: Any?,

    @SerializedName("trx_type")
    var trxType: String?,

    @SerializedName("remarks")
    var remarks: Any?,

    @SerializedName("amount")
    var amount: Int?,

    @SerializedName("ref_commission")
    var refCommission: Any?,

    @SerializedName("coin")
    var coin: Any?,

    @SerializedName("withdraw_account")
    var withdrawAccount: String?,

    @SerializedName("user_id")
    var userId: Int?,

    @SerializedName("image")
    var image: Any?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("created_at")
    var createdAt: String?,

    @SerializedName("updated_at")
    var updatedAt: String?
)

