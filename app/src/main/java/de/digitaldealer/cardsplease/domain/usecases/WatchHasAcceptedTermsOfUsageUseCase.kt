package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.database.KeyValueStore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

class WatchHasAcceptedTermsOfUsageUseCase : BaseUseCase() {

    private val keyValueStore by inject<KeyValueStore>()

    fun call(): Flow<Boolean> = keyValueStore.watchHasTermsOfUsageAccepted()
}
