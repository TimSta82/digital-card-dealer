package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.repositories.DeckRepository
import de.digitaldealer.cardsplease.domain.model.Deck
import org.koin.core.component.inject

class ShuffleDeckUseCase : BaseUseCase() {

    private val deckRepository by inject<DeckRepository>()

    suspend fun call(deckId: String) = simpleResponseCall(deckRepository.shuffleDeck(deckId = deckId)) { dto -> Deck(dto) }
}
