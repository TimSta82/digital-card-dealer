package de.bornholdtlee.defaultprojectkotlin.data.model


import com.google.gson.annotations.SerializedName

data class DeckResponseDTO(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("deck_id")
    val deckId: String?,
    @SerializedName("shuffled")
    val shuffled: Boolean?,
    @SerializedName("remaining")
    val remaining: Int?
)