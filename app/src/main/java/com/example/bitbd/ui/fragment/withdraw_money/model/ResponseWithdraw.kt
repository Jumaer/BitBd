package com.example.bitbd.ui.fragment.withdraw_money.model
import com.google.gson.annotations.SerializedName

data class ResponseWithdraw(@SerializedName("success")
                            var success: Boolean? ,

                            @SerializedName("data")
                            var data: List<BaseResponseWithdraw>? ,

                            @SerializedName("message")
                            var message: String? ,)



