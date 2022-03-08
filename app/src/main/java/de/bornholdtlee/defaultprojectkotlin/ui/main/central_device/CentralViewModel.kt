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

    private val _flop = MutableLiveData<List<Card?>>()
    val flop: LiveData<List<Card?>> = _flop

    private val _turn = MutableLiveData<List<Card?>>()
    val turn: LiveData<List<Card?>> = _turn

    private val _river = MutableLiveData<List<Card?>>()
    val river: LiveData<List<Card?>> = _river

    private val _onDeckLoadingFailure = SingleLiveEvent<Any>()
    val onDeckLoadingFailure: LiveData<Any> = _onDeckLoadingFailure

    private val _onDrawCardsFailure = SingleLiveEvent<Any>()
    val onDrawCardsFailure: LiveData<Any> = _onDrawCardsFailure

    private val _gamePhase = MutableLiveData(GamePhase.FLOP)
    val gamePhase: LiveData<GamePhase> = _gamePhase

    private var deckId: String? = null

    init {
        launch {
            when (val result = getNewDeckUseCase.call()) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject?.let { deckId = it.deckId }
                }
                else -> _onDeckLoadingFailure.callAsync()
            }
        }
    }

    fun deal(gamePhase: GamePhase) {
        launch {
            deckId?.let { deckId ->
                if (deckId.isNotBlank()) drawCards(deckId = deckId, gamePhase)
            }
        }
    }

    private suspend fun drawCards(deckId: String, gamePhase: GamePhase) {
        when (val result = drawAmountOfCardsUseCase.call(amount = gamePhase.amount, deckId = deckId)) {
            is BaseUseCase.UseCaseResult.Success -> result.resultObject?.let { cards ->
                when (gamePhase) {
                    GamePhase.FLOP -> _flop.postValue(cards)
                    GamePhase.TURN -> _turn.postValue(cards)
                    GamePhase.RIVER -> _river.postValue(cards)
                }
                updateGamePhase(gamePhase)
            }
            else -> _onDrawCardsFailure.callAsync()
        }
    }

    private fun updateGamePhase(gamePhase: GamePhase) = when (gamePhase) {
        GamePhase.FLOP -> _gamePhase.value = GamePhase.TURN
        GamePhase.TURN -> _gamePhase.value = GamePhase.RIVER
        GamePhase.RIVER -> _gamePhase.value = GamePhase.SHUFFLE
        GamePhase.SHUFFLE -> {
            clearCards()
            _gamePhase.value = GamePhase.FLOP
        }
    }

    private fun clearCards() {
        _flop.postValue(emptyList())
        _turn.postValue(emptyList())
        _river.postValue(emptyList())
    }

    fun addPlayer() {

    }
}

enum class GamePhase(val amount: Int, val buttonText: String) {
    FLOP(3, "Flop"), TURN(1, "Turn"), RIVER(1, "River"), SHUFFLE(0, "Shuffle")
}
