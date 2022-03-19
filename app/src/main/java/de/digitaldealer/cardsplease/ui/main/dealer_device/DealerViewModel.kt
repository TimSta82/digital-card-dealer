package de.digitaldealer.cardsplease.ui.main.dealer_device

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.COLLECTION_HAND_CARDS
import de.digitaldealer.cardsplease.COLLECTION_PLAYERS
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.domain.model.Deck
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.domain.usecases.BaseUseCase
import de.digitaldealer.cardsplease.domain.usecases.DrawAmountOfCardsUseCase
import de.digitaldealer.cardsplease.domain.usecases.GetNewDeckUseCase
import de.digitaldealer.cardsplease.domain.usecases.ShuffleDeckUseCase
import de.digitaldealer.cardsplease.extensions.second
import de.digitaldealer.cardsplease.ui.extensions.launch
import de.digitaldealer.cardsplease.ui.util.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DealerViewModel : ViewModel(), KoinComponent {

    private val getNewDeckUseCase by inject<GetNewDeckUseCase>()
    private val shuffleDeckUseCase by inject<ShuffleDeckUseCase>()
    private val drawAmountOfCardsUseCase by inject<DrawAmountOfCardsUseCase>()

    private val _deck = MutableLiveData<Deck>()
    val deck: LiveData<Deck> = _deck

    private val _flop = MutableLiveData<List<Card?>>()
    val flop: LiveData<List<Card?>> = _flop

    private val _turn = MutableLiveData<List<Card?>>()
    val turn: LiveData<List<Card?>> = _turn

    private val _river = MutableLiveData<List<Card?>>()
    val river: LiveData<List<Card?>> = _river

    private val _addPlayerWithDeckId = MutableLiveData<String?>()
    val addPlayerWithDeckId: LiveData<String?> = _addPlayerWithDeckId

    private val _onDeckLoadingFailure = SingleLiveEvent<Any>()
    val onDeckLoadingFailure: LiveData<Any> = _onDeckLoadingFailure

    private val _onDrawCardsFailure = SingleLiveEvent<Any>()
    val onDrawCardsFailure: LiveData<Any> = _onDrawCardsFailure

    private val _gamePhase = MutableLiveData(GamePhase.DEAL)
    val gamePhase: LiveData<GamePhase> = _gamePhase

    private val _joinedPlayers = MutableLiveData<List<Player>>()
    val joinedPlayers: LiveData<List<Player>> = _joinedPlayers

    private val _onPlayerCountError = SingleLiveEvent<PlayerCountError>()
    val onPlayerCountError: LiveData<PlayerCountError> = _onPlayerCountError

    private val _onShuffleError = SingleLiveEvent<Any>()
    val onShuffleError: LiveData<Any> = _onShuffleError

    private var deckId: String? = null
    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private var playersListener: ListenerRegistration? = null
    private var _remainingCards: List<Card> = emptyList()

    init {
        Logger.debug("init() called")
        launch {
            when (val result = getNewDeckUseCase.call()) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject?.let { deck ->
                        deckId = deck.deckId
                        initGameWithDeckId(deck)
                    }
                }
                else -> _onDeckLoadingFailure.callAsync()
            }
        }
    }

    fun onStart(deck: Deck) {
        Logger.debug("onStart() called - deckId: ${deck.deckId}")
        watchForPlayersJoining(deck.deckId)
    }

    fun onStop() {
        playersListener?.remove()
    }

    private fun watchForPlayersJoining(deckId: String) {
        playersListener?.remove()
        playersListener = gamesCollectionRef.document(deckId).collection(COLLECTION_PLAYERS).addSnapshotListener { snapshot, error ->
            val players = ArrayList<Player>()
            if (snapshot?.isEmpty?.not() == true) {
                for (doc in snapshot) {
                    val player = doc.toObject<Player>()
                    players.add(player)
                }
                Logger.debug("players: $players")
                when {
                    players.size in 2..9 -> _joinedPlayers.value = players
                    players.size == 1 -> _onPlayerCountError.value = PlayerCountError.NOT_ENOUGH
                    else -> _onPlayerCountError.value = PlayerCountError.TOO_MANY
                }
            }
            if (error != null) {
                Logger.debug("Loading player failed")
                return@addSnapshotListener
            }
        }
    }


    private fun initGameWithDeckId(deck: Deck) {
        gamesCollectionRef.document(deck.deckId).set(deck)
            .addOnSuccessListener {
                _deck.value = deck
                Logger.debug("Successfully init game -> deckId: ${deck.deckId}")
            }
            .addOnFailureListener {
                Logger.debug("Game init failed -> deckId: ${deck.deckId}")
            }
    }

    fun deal(gamePhase: GamePhase) {
        launch {
            deckId?.let { deckId ->
                if (deckId.isNotBlank())
                    when (gamePhase) {
                        GamePhase.DEAL -> shuffleCards(deckId, gamePhase)
                        else -> handleGamePhase(gamePhase)
                    }
            }
        }
    }

    private suspend fun shuffleCards(deckId: String, gamePhase: GamePhase) {
        when (val result = shuffleDeckUseCase.call(deckId = deckId)) {
            is BaseUseCase.UseCaseResult.Success -> if (result.resultObject.shuffled) drawCards(deckId = deckId, gamePhase) else _onShuffleError.callAsync()
            else -> _onShuffleError.callAsync()
        }
    }

    private fun handleGamePhase(gamePhase: GamePhase) {
        when (gamePhase) {
            GamePhase.FLOP -> _flop.postValue(getCardsAndHandleRemainingCardStack(gamePhase))
            GamePhase.TURN -> _turn.postValue(getCardsAndHandleRemainingCardStack(gamePhase))
            GamePhase.RIVER -> _river.postValue(getCardsAndHandleRemainingCardStack(gamePhase))
        }
        updateGamePhase(gamePhase)
    }

    private fun getCardsAndHandleRemainingCardStack(gamePhase: GamePhase): List<Card> {
        val dealAmount = if (gamePhase == GamePhase.FLOP) 3 else 1
        val burnedRemainingCards = _remainingCards.drop(1)
        val cardsToDeal = burnedRemainingCards.take(dealAmount)
        val postActionRemainingCards = burnedRemainingCards.drop(dealAmount)
        _remainingCards = postActionRemainingCards
        return cardsToDeal
    }

    private suspend fun drawCards(deckId: String, gamePhase: GamePhase) {
        _joinedPlayers.value?.let { players ->
            val totalCardsAmount = (players.size * 2) + 8
            when (val result = drawAmountOfCardsUseCase.call(amount = totalCardsAmount, deckId = deckId)) {
                is BaseUseCase.UseCaseResult.Success -> result.resultObject?.let { cards ->
                    dealHandCardsToPlayers(players, cards)
                    updateGamePhase(gamePhase)
                }
                else -> _onDrawCardsFailure.callAsync()
            }
        }
    }

    private fun dealHandCardsToPlayers(players: List<Player>, cards: List<Card>) {
        val remainingCards = cards
        Logger.debug("cards.size: ${cards.size}")
        Logger.debug("players.size: ${players.size}")
        players.forEach { player ->
            val currentHand = Hand(one = remainingCards.first(), two = remainingCards.second())
            gamesCollectionRef.document(player.deckId).collection(COLLECTION_PLAYERS).document(player.nickName).collection(COLLECTION_HAND_CARDS).document("currentHand").set(currentHand)
                .addOnSuccessListener {
                    Logger.debug("Spieler ${player.nickName} sollte jetzt ne hand haben")
                    val remainingCards = remainingCards.drop(2)
                    _remainingCards = remainingCards
                }
                .addOnFailureListener {
                    Logger.debug("Karten konnten nicht an Spieler ${player.nickName} verteilt werden")
                    return@addOnFailureListener
                }
        }
    }

    private fun updateGamePhase(gamePhase: GamePhase) = when (gamePhase) {
        GamePhase.DEAL -> _gamePhase.value = GamePhase.FLOP
        GamePhase.FLOP -> _gamePhase.value = GamePhase.TURN
        GamePhase.TURN -> _gamePhase.value = GamePhase.RIVER
        GamePhase.RIVER -> _gamePhase.value = GamePhase.SHUFFLE
        GamePhase.SHUFFLE -> {
            clearCards()
            _gamePhase.value = GamePhase.DEAL
        }
    }

    private fun clearCards() {
        deckId?.let {
            gamesCollectionRef.document(it).collection(COLLECTION_PLAYERS).document().collection(COLLECTION_HAND_CARDS).document().delete()
                .addOnSuccessListener {
                    Logger.debug("delete all player hand cards")
                    _flop.postValue(emptyList())
                    _turn.postValue(emptyList())
                    _river.postValue(emptyList())
                    _remainingCards = emptyList()
                }
                .addOnFailureListener {
                    Logger.debug("failed deleting all player hand cards")
                }
        }
    }

    fun addPlayer() {
        deckId?.let {
            _addPlayerWithDeckId.value = it
        }
    }

    fun reset() {
        updateGamePhase(GamePhase.SHUFFLE)
    }

    fun resetPlayerDeckId() {
        _addPlayerWithDeckId.value = null
    }
}

enum class GamePhase(val buttonText: String) {
    DEAL("Dealen"), FLOP("Flop"), TURN("Turn"), RIVER("River"), SHUFFLE("Shuffle")
}

enum class PlayerCountError(val errorMessageId: Int) {
    NOT_ENOUGH(R.string.player_count_error_not_enough_message), TOO_MANY(R.string.player_count_error_too_many_message)
}
