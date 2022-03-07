package de.bornholdtlee.defaultprojectkotlin.domain.model

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
}
