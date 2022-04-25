package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.database.KeyValueStore
import org.koin.core.component.inject

class DeletePlayerLocallyUseCase : BaseUseCase() {

    private val keyValueStore by inject<KeyValueStore>()

    suspend fun call() {
        keyValueStore.deletePlayer()
    }
}
