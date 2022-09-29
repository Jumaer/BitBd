package com.example.bitbd.ui.fragment.accounts.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditInformationObject(
    @SerializedName("id")
    var id: Int?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("account")
    var account: String?,

    @SerializedName("branch")
    var branch: String?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("user_id")
    var userId: Int?,

    @SerializedName("created_at")
    var createdAt: String?,

    @SerializedName("updated_at")
    var updatedAt: String?
) : Parcelable


