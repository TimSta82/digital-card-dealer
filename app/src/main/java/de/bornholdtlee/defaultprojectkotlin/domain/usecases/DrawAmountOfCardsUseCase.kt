package de.bornholdtlee.defaultprojectkotlin.domain.usecases

import de.bornholdtlee.defaultprojectkotlin.data.repositories.DeckRepository
import de.bornholdtlee.defaultprojectkotlin.domain.model.Card
import org.koin.core.component.inject

class DrawAmountOfCardsUseCase : BaseUseCase() {

    private val deckRepository by inject<DeckRepository>()

    suspend fun call(amount: Int, deckId: String) = simpleResponseCall(deckRepository.drawAmountOfCards(amount = amount, deckId = deckId)) { dto ->
        dto.cards?.map { cardDto ->
            cardDto?.let { Card(it) }
        } ?: emptyList<Card>()
    }
}
