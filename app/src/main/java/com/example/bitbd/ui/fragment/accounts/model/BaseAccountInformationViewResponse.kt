package com.example.bitbd.ui.fragment.accounts.model

import com.example.bitbd.ui.fragment.affiliate.model.AffiliateObject
import com.google.gson.annotations.SerializedName

data class BaseAccountInformationViewResponse (@SerializedName("success")
                                               var success: Boolean?,

                                               @SerializedName("data")
                                               var data: List<AccountViewObject?>?,

                                               @SerializedName("message")
                                               var message: String? = null)