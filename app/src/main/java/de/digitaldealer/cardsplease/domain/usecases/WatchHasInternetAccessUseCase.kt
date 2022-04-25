@file:OptIn(ExperimentalComposeUiApi::class, kotlinx.coroutines.FlowPreview::class)

package de.digitaldealer.cardsplease.domain.usecases

import androidx.compose.ui.ExperimentalComposeUiApi
import de.digitaldealer.cardsplease.data.controller.NetworkStateController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class WatchHasInternetAccessUseCase : BaseUseCase() {

    private val networkController by inject<NetworkStateController>()

    fun call(): Flow<Boolean> {
        return networkController.activeNetworks.map { activeNetworks ->
            activeNetworks.any { activeNetwork -> activeNetwork.hasInternetAccess }
        }
    }
}
