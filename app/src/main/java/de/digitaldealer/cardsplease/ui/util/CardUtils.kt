package de.digitaldealer.cardsplease.ui.util

import de.digitaldealer.cardsplease.R

object CardUtils {

    fun getSuitIcon(suit: String) = when (suit) {
        "HEARTS" -> R.drawable.ic_hearts2
        "SPADES" -> R.drawable.ic_spades2
        "CLUBS" -> R.drawable.ic_clubs2
        "DIAMONDS" -> R.drawable.ic_diamonds2
        else -> R.drawable.ic_diamonds2
    }

    fun getSuitColor(suit: String) = when (suit) {
        "HEARTS", "DIAMONDS" -> R.color.card_red
        else -> R.color.black
    }
}