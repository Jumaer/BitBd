package com.example.bitbd.ui.fragment.affiliate.model
import com.google.gson.annotations.SerializedName


data class UserAffiliate (@SerializedName("id")
                          var id: Int? ,

                          @SerializedName("name")
                          var name: String? )


