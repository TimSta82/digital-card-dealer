package de.digitaldealer.cardsplease.ui.main.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.digitaldealer.cardsplease.domain.usecases.DeletePlayerLocallyUseCase
import de.digitaldealer.cardsplease.domain.usecases.SetAcceptTermsOfUsageUseCase
import de.digitaldealer.cardsplease.domain.usecases.WatchHasAcceptedTermsOfUsageUseCase
import de.digitaldealer.cardsplease.domain.usecases.WatchPlayerLocallyUseCase
import de.digitaldealer.cardsplease.ui.extensions.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StartViewModel : ViewModel(), KoinComponent {

    private val watchPlayerLocallyUseCase by inject<WatchPlayerLocallyUseCase>()
    private val deletePlayerLocallyUseCase by inject<DeletePlayerLocallyUseCase>()
    private val setAcceptTermsOfUsageUseCase by inject<SetAcceptTermsOfUsageUseCase>()
    private val watchHasAcceptedTermsOfUsageUseCase by inject<WatchHasAcceptedTermsOfUsageUseCase>()

    private val _alternatingColors = MutableStateFlow(true)
    val alternatingColors = _alternatingColors.asStateFlow()

    val localPlayer = watchPlayerLocallyUseCase.call().shareIn(viewModelScope, SharingStarted.Lazily)
    val hasAcceptedTermsOfUsageUseCase = watchHasAcceptedTermsOfUsageUseCase.call().shareIn(viewModelScope, SharingStarted.Lazily)

    init {
        animateSymbolColors()
    }

    fun onDeclineJoinTable() {
        launch { deletePlayerLocallyUseCase.call() }
    }

    private fun animateSymbolColors() {
        launch {
            while (true) {
                delay(1000L)
                _alternatingColors.value = _alternatingColors.value.not()
            }
        }
    }

    fun acceptTermsOfUsage() {
        launch { setAcceptTermsOfUsageUseCase.call() }
    }
}
