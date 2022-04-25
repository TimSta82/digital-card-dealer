package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.database.KeyValueStore
import de.digitaldealer.cardsplease.domain.model.Player
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

class WatchPlayerLocallyUseCase : BaseUseCase() {

    private val keyValueStore by inject<KeyValueStore>()

    fun call(): Flow<Player> = keyValueStore.watchPlayer()
}
