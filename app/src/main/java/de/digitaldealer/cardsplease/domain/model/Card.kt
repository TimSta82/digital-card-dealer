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
    val code: String
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
}
