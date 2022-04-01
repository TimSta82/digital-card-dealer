package de.digitaldealer.cardsplease

import de.digitaldealer.cardsplease.domain.model.DeckHelper
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.extensions.second
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

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

    @Test
    fun deal_hand_card_to_two_players() {
        val cards = DeckHelper.getRandomCardsByPlayerCount(2).toList()
        val hand1 = Hand(cards.first(), cards.second(), round = 0)
        val hand2 = Hand(cards[2], cards[3], round = 0)

        assertTrue { hand1.one != hand1.two }
        assertTrue { hand2.one != hand2.two }

        assertTrue { hand1.one != hand2.one }
        assertTrue { hand1.one != hand2.two }
        assertTrue { hand1.two != hand2.two }
        assertTrue { hand1.two != hand2.one }
    }
}
