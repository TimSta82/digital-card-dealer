package de.bornholdtlee.defaultprojectkotlin.ui.main.central_device

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.bornholdtlee.defaultprojectkotlin.domain.model.Card
import de.bornholdtlee.defaultprojectkotlin.domain.model.Deck
import de.bornholdtlee.defaultprojectkotlin.domain.usecases.BaseUseCase
import de.bornholdtlee.defaultprojectkotlin.domain.usecases.DrawAmountOfCardsUseCase
import de.bornholdtlee.defaultprojectkotlin.domain.usecases.GetNewDeckUseCase
import de.bornholdtlee.defaultprojectkotlin.extensions.launch
import de.bornholdtlee.defaultprojectkotlin.ui.util.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CentralViewModel : ViewModel(), KoinComponent {

    private val getNewDeckUseCase by inject<GetNewDeckUseCase>()
    private val drawAmountOfCardsUseCase by inject<DrawAmountOfCardsUseCase>()

    private val _deck = MutableLiveData<Deck>()
    val deck: LiveData<Deck> = _deck

    private val _cards = MutableLiveData<List<Card?>>()
    val cards : LiveData<List<Card?>> = _cards

    private val _onDeckLoadingFailure = SingleLiveEvent<Any>()
    val onDeckLoadingFailure: LiveData<Any> = _onDeckLoadingFailure

    private val _onDrawCardsFailure = SingleLiveEvent<Any>()
    val onDrawCardsFailure : LiveData<Any> = _onDrawCardsFailure

    init {
        launch {
            when (val result = getNewDeckUseCase.call()) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject?.let { drawCards(it.deckId) }
                }
                else -> _onDeckLoadingFailure.callAsync()
            }
        }
    }

    suspend fun drawCards(deckId: String) {
        when (val result = drawAmountOfCardsUseCase.call(amount = 3, deckId = deckId)) {
            is BaseUseCase.UseCaseResult.Success -> result.resultObject?.let { _cards.postValue(it) }
            else -> _onDrawCardsFailure.callAsync()
        }
    }
}
