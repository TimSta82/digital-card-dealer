package de.digitaldealer.cardsplease.domain.model

data class Game(
    val deck: Deck?,
    val players: List<Player>? = null,
    val round: Int? = -1
) {
    constructor() : this(
        deck = null,
        players = null,
        round = -1
    )
}