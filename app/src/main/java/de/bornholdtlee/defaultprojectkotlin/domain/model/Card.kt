package de.bornholdtlee.defaultprojectkotlin.domain.model

import de.bornholdtlee.defaultprojectkotlin.R
import de.bornholdtlee.defaultprojectkotlin.data.model.CardResponseDTO

data class Card(
    val image: String,
    val value: String,
    val suit: String,
    val code: String
) {
    constructor(dto: CardResponseDTO) : this(
        image = dto.image ?: "-1",
        value = dto.value ?: "-1",
        suit = dto.suit ?: "-1",
        code = dto.code ?: "-1"
    )

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
