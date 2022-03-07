package de.bornholdtlee.defaultprojectkotlin.data.model

import com.google.gson.annotations.SerializedName

data class CardResponseDTO(
    @SerializedName("image")
    val image: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("suit")
    val suit: String?,
    @SerializedName("code")
    val code: String?
)