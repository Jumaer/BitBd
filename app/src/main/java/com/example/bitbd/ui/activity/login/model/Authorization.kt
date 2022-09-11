package com.example.bitbd.ui.activity.login.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Authorization (@SerializedName("token")
                          var token: String? ,
                          @SerializedName("type")
                          var type: String? ) : Parcelable













