package de.digitaldealer.cardsplease.domain.model

import com.google.firebase.firestore.PropertyName
import de.digitaldealer.cardsplease.data.model.DeckResponseDTO

data class Deck(
    @PropertyName("success")
    val success: Boolean,
    @PropertyName("deck_id")
    val deckId: String,
    @PropertyName("shuffled")
    val shuffled: Boolean,
    @PropertyName("remaining")
    val remaining: Int
) {
    constructor() : this(
        success = false,
        deckId = "",
        shuffled = false,
        remaining = -1
    )

    constructor(dto: DeckResponseDTO) : this(
        success = dto.success ?: false,
        deckId = dto.deckId ?: "-1",
        shuffled = dto.shuffled ?: false,
        remaining = dto.remaining ?: -1
    )
}
