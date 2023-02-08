package dev.atick.network.data.models

import com.google.gson.annotations.SerializedName

data class GetItemResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)