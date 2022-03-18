package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.repositories.DeckRepository
import de.digitaldealer.cardsplease.domain.model.Card
import org.koin.core.component.inject

class DrawAmountOfCardsUseCase : BaseUseCase() {

    private val deckRepository by inject<DeckRepository>()

    suspend fun call(amount: Int, deckId: String) = simpleResponseCall(deckRepository.drawAmountOfCards(amount = amount, deckId = deckId)) { dto ->
        dto.cards?.map { cardDto ->
            cardDto?.let { Card(it) } ?: Card()
        } ?: emptyList()
    }
}
