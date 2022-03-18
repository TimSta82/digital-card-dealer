package de.digitaldealer.cardsplease.domain.model

import com.google.firebase.firestore.PropertyName
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.data.model.CardResponseDTO

data class Card(
    @PropertyName("image")
    val image: String,
    @PropertyName("value")
    val value: String,
    @PropertyName("suit")
    val suit: String,
    @PropertyName("code")
    val code: String,
) {
    constructor(dto: CardResponseDTO) : this(
        image = dto.image ?: "-1",
        value = dto.value ?: "-1",
        suit = dto.suit ?: "-1",
        code = dto.code ?: "-1"
    )

    constructor() : this(
        image = "",
        value = "",
        suit = "",
        code = ""
    )

    companion object {
        fun getDefaultCard() = Card(image = "", value = "JOKER", suit = "", code = "JOKER")
    }

    fun getSuitIcon() = when (suit) {
        "HEARTS" -> R.drawable.ic_hearts
        "SPADES" -> R.drawable.ic_spades
        "CLUBS" -> R.drawable.ic_clubs
        "DIAMONDS" -> R.drawable.ic_diamonds
        else -> R.drawable.ic_diamonds
    }

    fun getSuitColor() = when (suit) {
        "HEARTS", "DIAMONDS" -> R.color.card_red
        else -> R.color.black
    }
}
