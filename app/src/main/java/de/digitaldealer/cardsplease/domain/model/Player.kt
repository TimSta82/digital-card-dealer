package de.digitaldealer.cardsplease.domain.model

data class Player(
    val deckId: String,
    val nickName: String,
) {
    constructor() : this(
        deckId = "-1",
        nickName = ""
    )
}