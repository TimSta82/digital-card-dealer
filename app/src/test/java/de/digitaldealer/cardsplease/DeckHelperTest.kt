package de.digitaldealer.cardsplease

import de.digitaldealer.cardsplease.domain.model.DeckHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class DeckHelperTest : BaseTest() {

    @Test
    fun get_correct_amount_of_cards_for_two_players() {
        val cards = DeckHelper.getRandomCardsByPlayerCount(2)
        assertEquals(12, cards.size)
    }

    @Test
    fun get_correct_amount_of_cards_for_four_players() {
        val cards = DeckHelper.getRandomCardsByPlayerCount(4)
        assertEquals(16, cards.size)
    }

    @Test
    fun get_correct_amount_of_cards_for_ten_players() {
        val cards = DeckHelper.getRandomCardsByPlayerCount(10)
        assertEquals(28, cards.size)
    }

    @Test
    fun get_no_cards_while_asking_for_too_many_cards() {
        val cards = DeckHelper.getRandomCardsByAmount(53)
        assertEquals(0, cards.size)
    }

    @Test
    fun get_no_cards_while_asking_for_no_cards() {
        val cards = DeckHelper.getRandomCardsByAmount(0)
        assertEquals(0, cards.size)
    }

    @Test
    fun get_complete_shuffled_deck() {
        val cards = DeckHelper.getRandomCardsByAmount(52)
        assertEquals(52, cards.size)
    }
}
