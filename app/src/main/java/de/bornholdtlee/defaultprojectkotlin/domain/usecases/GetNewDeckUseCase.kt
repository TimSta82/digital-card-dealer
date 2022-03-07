package de.bornholdtlee.defaultprojectkotlin.domain.usecases

import de.bornholdtlee.defaultprojectkotlin.data.repositories.DeckRepository
import de.bornholdtlee.defaultprojectkotlin.domain.model.Deck
import org.koin.core.component.inject

class GetNewDeckUseCase : BaseUseCase() {

    private val deckRepository by inject<DeckRepository>()

    suspend fun call() = simpleResponseCall(deckRepository.getNewDeck()) { dto -> Deck(dto) }
}