package de.digitaldealer.cardsplease.ui.util

import de.digitaldealer.cardsplease.R

object CardUtils {

    fun getSuitIcon(suit: String) = when (suit) {
        "HEARTS" -> R.drawable.ic_hearts
        "SPADES" -> R.drawable.ic_spades
        "CLUBS" -> R.drawable.ic_clubs
        "DIAMONDS" -> R.drawable.ic_diamonds
        else -> R.drawable.ic_diamonds
    }

    fun getSuitColor(suit: String) = when (suit) {
        "HEARTS", "DIAMONDS" -> R.color.card_red
        else -> R.color.black
    }
}