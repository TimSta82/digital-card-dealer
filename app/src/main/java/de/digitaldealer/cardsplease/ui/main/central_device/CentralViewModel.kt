package de.digitaldealer.cardsplease.ui.main.central_device

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.domain.model.Deck
import de.digitaldealer.cardsplease.domain.model.Game
import de.digitaldealer.cardsplease.domain.usecases.BaseUseCase
import de.digitaldealer.cardsplease.domain.usecases.DrawAmountOfCardsUseCase
import de.digitaldealer.cardsplease.domain.usecases.GetNewDeckUseCase
import de.digitaldealer.cardsplease.extensions.launch
import de.digitaldealer.cardsplease.ui.util.SingleLiveEvent
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

    private val _addPlayerWithDeckId = MutableLiveData<String?>()
    val addPlayerWithDeckId: LiveData<String?> = _addPlayerWithDeckId

    private val _onDeckLoadingFailure = SingleLiveEvent<Any>()
    val onDeckLoadingFailure: LiveData<Any> = _onDeckLoadingFailure

    private val _onDrawCardsFailure = SingleLiveEvent<Any>()
    val onDrawCardsFailure: LiveData<Any> = _onDrawCardsFailure

    private val _gamePhase = MutableLiveData(GamePhase.FLOP)
    val gamePhase: LiveData<GamePhase> = _gamePhase

    private val _onPlayerJoinedSuccessful = SingleLiveEvent<Game>()
    val onPlayerJoinedSuccessful: LiveData<Game> = _onPlayerJoinedSuccessful

    private var deckId: String? = null
    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private val gamesDeckRef = db.collection("games").document("deck")
    private val playersRef = db.collection("players").document("player")
    private var gameListenerRegistration: ListenerRegistration? = null

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
        Logger.debug("onStart() called - deckId: ${deck?.deckId ?: "-1"}")
        gameListenerRegistration?.remove()
        gameListenerRegistration = gamesCollectionRef.document(deck.deckId).addSnapshotListener { snapshot, error ->
            if (snapshot?.exists() == true) {
                val game = snapshot.toObject<Game>()
                Logger.debug("observe snapshot -> $snapshot")
                Logger.debug("observe player successfull -> ${game?.players ?: listOf("keiner da")}")
                game?.let { _onPlayerJoinedSuccessful.value = it }
            }
            if (error != null) {
                Logger.debug("Loading player failed")
            }
        }
    }

    fun onStop() {
        gameListenerRegistration?.remove()
    }

    private fun initGameWithDeckId(deck: Deck) {
        gamesCollectionRef.document(deck.deckId).set(Game(deck = deck))
            .addOnSuccessListener {
//                _onUploadDeckSuccessful.call()
                _deck.value = deck
                Logger.debug("Successfully init game -> deckId: ${deck.deckId}")
            }
            .addOnFailureListener {
                Logger.debug("Game init failed -> deckId: ${deck.deckId}")
            }
    }

//    private fun loadFromFireStore() {
//        playersRef.get()
//            .addOnSuccessListener { snapshot ->
//                if (snapshot.exists()) {
//                    _onPlayerJoinedSuccessful.value = snapshot[KEY_PLAYER] as? Player
//                } else {
//                    Logger.debug("Loading player failed, coz snapshot does not exists")
//                }
//            }
//            .addOnFailureListener {
//                Logger.debug("Loading player failed")
//            }
//    }

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

enum class GamePhase(val amount: Int, val buttonText: String) {
    FLOP(3, "Flop"), TURN(1, "Turn"), RIVER(1, "River"), SHUFFLE(0, "Shuffle")
}
