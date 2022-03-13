package de.digitaldealer.cardsplease.domain.model

import de.digitaldealer.cardsplease.data.model.DeckResponseDTO

data class Deck(
    val success: Boolean,
    val deckId: String,
    val shuffled: Boolean,
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
