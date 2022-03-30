package de.digitaldealer.cardsplease.domain.model

import kotlin.random.Random

object DeckHelper {

    private const val DIAMONDS = "DIAMONDS"
    private const val CLUBS = "CLUBS"
    private const val HEARTS = "HEARTS"
    private const val SPADES = "SPADES"
    private val deck = listOf<Card>(
        // Spades
        Card(image = "", code = "AS", value = "ACE", suit = SPADES),
        Card(image = "", code = "2S", value = "2", suit = SPADES),
        Card(image = "", code = "3S", value = "3", suit = SPADES),
        Card(image = "", code = "4S", value = "4", suit = SPADES),
        Card(image = "", code = "5S", value = "5", suit = SPADES),
        Card(image = "", code = "6S", value = "6", suit = SPADES),
        Card(image = "", code = "7S", value = "7", suit = SPADES),
        Card(image = "", code = "8S", value = "8", suit = SPADES),
        Card(image = "", code = "9S", value = "9", suit = SPADES),
        Card(image = "", code = "0S", value = "10", suit = SPADES),
        Card(image = "", code = "JS", value = "JACK", suit = SPADES),
        Card(image = "", code = "QS", value = "QUEEN", suit = SPADES),
        Card(image = "", code = "KS", value = "KING", suit = SPADES),
        // Clubs
        Card(image = "", code = "AC", value = "ACE", suit = CLUBS),
        Card(image = "", code = "2C", value = "2", suit = CLUBS),
        Card(image = "", code = "3C", value = "3", suit = CLUBS),
        Card(image = "", code = "4C", value = "4", suit = CLUBS),
        Card(image = "", code = "5C", value = "5", suit = CLUBS),
        Card(image = "", code = "6C", value = "6", suit = CLUBS),
        Card(image = "", code = "7C", value = "7", suit = CLUBS),
        Card(image = "", code = "8C", value = "8", suit = CLUBS),
        Card(image = "", code = "9C", value = "9", suit = CLUBS),
        Card(image = "", code = "0C", value = "10", suit = CLUBS),
        Card(image = "", code = "JC", value = "JACK", suit = CLUBS),
        Card(image = "", code = "QC", value = "QUEEN", suit = CLUBS),
        Card(image = "", code = "KC", value = "KING", suit = CLUBS),
        // Diamonds
        Card(image = "", code = "AD", value = "ACE", suit = DIAMONDS),
        Card(image = "", code = "2D", value = "2", suit = DIAMONDS),
        Card(image = "", code = "3D", value = "3", suit = DIAMONDS),
        Card(image = "", code = "4D", value = "4", suit = DIAMONDS),
        Card(image = "", code = "5D", value = "5", suit = DIAMONDS),
        Card(image = "", code = "6D", value = "6", suit = DIAMONDS),
        Card(image = "", code = "7D", value = "7", suit = DIAMONDS),
        Card(image = "", code = "8D", value = "8", suit = DIAMONDS),
        Card(image = "", code = "9D", value = "9", suit = DIAMONDS),
        Card(image = "", code = "0D", value = "10", suit = DIAMONDS),
        Card(image = "", code = "JD", value = "JACK", suit = DIAMONDS),
        Card(image = "", code = "QD", value = "QUEEN", suit = DIAMONDS),
        Card(image = "", code = "KD", value = "KING", suit = DIAMONDS),
        // Hearts
        Card(image = "", code = "AH", value = "ACE", suit = HEARTS),
        Card(image = "", code = "2H", value = "2", suit = HEARTS),
        Card(image = "", code = "3H", value = "3", suit = HEARTS),
        Card(image = "", code = "4H", value = "4", suit = HEARTS),
        Card(image = "", code = "5H", value = "5", suit = HEARTS),
        Card(image = "", code = "6H", value = "6", suit = HEARTS),
        Card(image = "", code = "7H", value = "7", suit = HEARTS),
        Card(image = "", code = "8H", value = "8", suit = HEARTS),
        Card(image = "", code = "9H", value = "9", suit = HEARTS),
        Card(image = "", code = "0H", value = "10", suit = HEARTS),
        Card(image = "", code = "JH", value = "JACK", suit = HEARTS),
        Card(image = "", code = "QH", value = "QUEEN", suit = HEARTS),
        Card(image = "", code = "KH", value = "KING", suit = HEARTS),
    )

    private fun getDeck(): List<Card> = deck

    fun getSpadesCard(): Card = deck[0]
    fun getClubsCard(): Card = deck[15]
    fun getDiamondsCard(): Card = deck[30]

    fun getRandomCardsByPlayerCount(playerCount: Int): Set<Card> {
        val amount = (playerCount * 2) + 8
        val randomCards = mutableSetOf<Card>()
        while (randomCards.size != amount) {
            val randomCard = getDeck()[Random.nextInt(52)]
            if (randomCards.contains(randomCard).not()) randomCards.add(randomCard)
        }
        return if (randomCards.size == amount) randomCards else emptySet<Card>()
    }

    fun getRandomCardsByAmount(amount: Int): Set<Card> {
        val randomCards = mutableSetOf<Card>()
        while (randomCards.size != amount) {
            if (amount > 52) break
            val randomCard = getDeck()[Random.nextInt(52)]
            if (randomCards.contains(randomCard).not()) randomCards.add(randomCard)
        }
        return if (randomCards.size == amount) randomCards else emptySet<Card>()
    }
}
