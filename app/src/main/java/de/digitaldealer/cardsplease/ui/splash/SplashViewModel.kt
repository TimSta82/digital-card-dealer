package de.digitaldealer.cardsplease.ui.splash

import androidx.lifecycle.*
import de.digitaldealer.cardsplease.domain.usecases.SetAcceptTermsOfUsageUseCase
import de.digitaldealer.cardsplease.domain.usecases.WatchHasAcceptedTermsOfUsageUseCase
import de.digitaldealer.cardsplease.ui.extensions.launch
import de.digitaldealer.cardsplease.ui.extensions.stateFlow
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SplashViewModel : ViewModel(), KoinComponent {

    private val setAcceptTermsOfUsageUseCase by inject<SetAcceptTermsOfUsageUseCase>()
    private val watchHasAcceptedTermsOfUsageUseCase by inject<WatchHasAcceptedTermsOfUsageUseCase>()

        val hasAcceptedTermsOfUsageUseCase = stateFlow(flow = watchHasAcceptedTermsOfUsageUseCase.call(), initialValue = false)
//    val hasAccepted: LiveData<Boolean> = watchHasAcceptedTermsOfUsageUseCase.call().asLiveData(viewModelScope.coroutineContext)

    fun acceptTermsOfUsage(hasAccepted: Boolean) {
        launch { setAcceptTermsOfUsageUseCase.call(hasAccepted = hasAccepted) }
    }

    fun resetTermsOfUsage() {
        launch { setAcceptTermsOfUsageUseCase.call(hasAccepted = false) }
    }

    fun checkTermsAccepted() {
//        if (hasAcceptedTermsOfUsageUseCase.value.not()) launch { _onShowTermsDialog.emit(Unit) }
    }
}
