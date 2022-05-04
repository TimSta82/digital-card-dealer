package de.digitaldealer.cardsplease.ui.terms_of_usage

import androidx.lifecycle.*
import de.digitaldealer.cardsplease.domain.usecases.SetAcceptTermsOfUsageUseCase
import de.digitaldealer.cardsplease.domain.usecases.WatchHasAcceptedTermsOfUsageUseCase
import de.digitaldealer.cardsplease.ui.extensions.launch
import de.digitaldealer.cardsplease.ui.extensions.stateFlow
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TermsOfUsageViewModel : ViewModel(), KoinComponent {

    private val setAcceptTermsOfUsageUseCase by inject<SetAcceptTermsOfUsageUseCase>()
    private val watchHasAcceptedTermsOfUsageUseCase by inject<WatchHasAcceptedTermsOfUsageUseCase>()

        val hasAcceptedTermsOfUsageUseCase = stateFlow(flow = watchHasAcceptedTermsOfUsageUseCase.call(), initialValue = false)
//    val hasAccepted: LiveData<Boolean> = watchHasAcceptedTermsOfUsageUseCase.call().asLiveData(viewModelScope.coroutineContext)

    fun acceptTermsOfUsage() {
        launch { setAcceptTermsOfUsageUseCase.call(hasAccepted = true) }
    }

    fun resetTermsOfUsage() {
        launch { setAcceptTermsOfUsageUseCase.call(hasAccepted = false) }
    }
}
