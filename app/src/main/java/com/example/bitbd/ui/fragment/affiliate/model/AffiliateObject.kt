package com.example.bitbd.ui.fragment.affiliate.model
import com.google.gson.annotations.SerializedName


data class AffiliateObject(
    @SerializedName("id")
    var id: Int?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("trx_name")
    var trxName: String?,

    @SerializedName("trx_account")
    var trxAccount: String?,

    @SerializedName("trx_id")
    var trxId: String?,

    @SerializedName("trx_type")
    var trxType: String?,

    @SerializedName("remarks")
    var remarks: Any?,

    @SerializedName("amount")
    var amount: Int? = null,

    @SerializedName("ref_commission")
    var refCommission: Int?,

    @SerializedName("coin")
    var coin: Int?,

    @SerializedName("withdraw_account")
    var withdrawAccount: Any?,

    @SerializedName("user_id")
    var userId: Int?,

    @SerializedName("image")
    var image: String?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("created_at")
    var createdAt: String?,

    @SerializedName("updated_at")
    var updatedAt: String?,

    @SerializedName("user")
    var user: UserAffiliate?,
)

