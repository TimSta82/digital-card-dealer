package de.digitaldealer.cardsplease.domain.model

import java.util.*

data class Player(
    val deckId: String,
    val nickName: String,
    val uuid: UUID?
) {
    constructor() : this(
        deckId = "-1",
        nickName = "",
        uuid = null
    )
}