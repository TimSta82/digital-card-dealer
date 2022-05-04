package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.database.KeyValueStore
import de.digitaldealer.cardsplease.domain.model.Player
import org.koin.core.component.inject

class SetAcceptTermsOfUsageUseCase : BaseUseCase() {

    private val keyValueStore by inject<KeyValueStore>()

    suspend fun call(hasAccepted: Boolean) {
        keyValueStore.setHasTermsOfUsageAccepted(hasAccepted = hasAccepted)
    }
}
