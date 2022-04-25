package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.database.KeyValueStore
import de.digitaldealer.cardsplease.domain.model.Player
import org.koin.core.component.inject

class SetPlayerLocallyUseCase : BaseUseCase() {

    private val keyValueStore by inject<KeyValueStore>()

    suspend fun call(player: Player) {
        keyValueStore.setPlayer(player = player)
    }
}
