package com.example.bitbd.ui.fragment.withdraw_money.model

import com.google.gson.annotations.SerializedName


data class BaseAccountWithdrawResponse(@SerializedName("accounts")
                                       var accounts: List<WithdrawAccountResponse>? ,

                                       @SerializedName("max_withdraw")
                                       var maxWithdraw: String? ,

                                       @SerializedName("withdraw_date")
                                       var withdrawDate: List<String>? )

