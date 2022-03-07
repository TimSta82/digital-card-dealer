package de.bornholdtlee.defaultprojectkotlin.domain.model

import de.bornholdtlee.defaultprojectkotlin.data.model.DeckResponseDTO

data class Deck(
    val success: Boolean,
    val deckId: String,
    val shuffled: Boolean,
    val remaining: Int
) {
    constructor(dto: DeckResponseDTO) : this(
        success = dto.success ?: false,
        deckId = dto.deckId ?: "-1",
        shuffled = dto.shuffled ?: false,
        remaining = dto.remaining ?: -1
    )
}
