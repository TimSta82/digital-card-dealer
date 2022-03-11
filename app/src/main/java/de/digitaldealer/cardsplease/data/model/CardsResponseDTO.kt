package de.digitaldealer.cardsplease.data.model


import com.google.gson.annotations.SerializedName

data class CardsResponseDTO(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("cards")
    val cards: List<CardResponseDTO?>?,
    @SerializedName("deck_id")
    val deckId: String?,
    @SerializedName("remaining")
    val remaining: Int?
)
