package de.digitaldealer.cardsplease.ui.main.dealer_device

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.*
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.*
import de.digitaldealer.cardsplease.domain.usecases.WatchHasInternetAccessUseCase
import de.digitaldealer.cardsplease.extensions.second
import de.digitaldealer.cardsplease.ui.extensions.launch
import de.digitaldealer.cardsplease.ui.extensions.stateFlow
import de.digitaldealer.cardsplease.ui.util.TableNames
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class DealerViewModel : ViewModel(), KoinComponent {

    companion object {
        private val defaultBoard = listOf(Card(), Card(), Card(), Card(), Card())
    }

    private val watchHasInternetAccessUseCase by inject<WatchHasInternetAccessUseCase>()

    private val _table = MutableStateFlow(PokerTable())
    val table = _table.asStateFlow()

    private val _boardCards = MutableStateFlow(defaultBoard)
    val boardCards = _boardCards.asStateFlow()

    private val _gamePhase = MutableStateFlow(GamePhase.DEAL)
    val gamePhase = _gamePhase.asStateFlow()

    private val _joinedPlayers = MutableStateFlow(listOf(Player()))
    val joinedPlayers = _joinedPlayers.asStateFlow()

    private val _onPlayerCountError = MutableSharedFlow<PlayerCountError>()
    val onPlayerCountError = _onPlayerCountError.asSharedFlow()

    private val _onDealingCardsToPlayersAccomplished = MutableSharedFlow<Unit>()
    val onDealingCardsToPlayersAccomplished = _onDealingCardsToPlayersAccomplished.asSharedFlow()

    private val _onPlaySound = MutableSharedFlow<Unit>()
    val onPlaySound = _onPlaySound.asSharedFlow()

    private val _onNavigateBack = MutableSharedFlow<Unit>()
    val onNavigateBack = _onNavigateBack.asSharedFlow()

    private val _onShowErrorMessage = MutableSharedFlow<String>()
    val onShowErrorMessage = _onShowErrorMessage.asSharedFlow()

    private val _round = MutableStateFlow(0)
    val round = _round.asStateFlow()

    val hasInternetAccess: StateFlow<Boolean> = stateFlow(flow = watchHasInternetAccessUseCase.call(), initialValue = true)

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private var playersListener: ListenerRegistration? = null
    private var remainingCards: List<Card> = emptyList()
    private var phaseDependingCards = mutableListOf<Card>()
    private var pokerTable: PokerTable? = null

    init {
        pokerTable = PokerTable(tableId = UUID.randomUUID().toString().take(UUID_COUNT), tableName = TableNames.getRandomTableName())
        launch {
            initPokerTable(table = pokerTable!!)
        }
    }

    fun onStart() {
        watchForPlayersJoining(pokerTable!!.tableId)
    }

    fun onStop() {
        playersListener?.remove()
    }

    private fun watchForPlayersJoining(tableId: String) {
        playersListener?.remove()
        playersListener = gamesCollectionRef.document(tableId).collection(COLLECTION_PLAYERS).addSnapshotListener { snapshot, error ->
            val players = ArrayList<Player>()
            if (snapshot?.isEmpty?.not() == true) {
                for (doc in snapshot) {
                    val player = doc.toObject<Player>()
                    players.add(player)
                }
                Logger.debug("players: $players")
                when (players.size) {
                    in 2..10 -> _joinedPlayers.value = players
                    1 -> {
                        _joinedPlayers.value = players
                        launch { _onPlayerCountError.emit(PlayerCountError.NOT_ENOUGH) }
                        Logger.debug("Not enough players yet")
                    }
                    else -> {
                        launch { _onPlayerCountError.emit(PlayerCountError.TOO_MANY) }
                        Logger.debug("Too many players")
                    }
                }
            }
            if (error != null) {
                Logger.debug("Loading player failed")
                return@addSnapshotListener
            }
        }
    }

    private fun initPokerTable(table: PokerTable) {
        gamesCollectionRef.document(table.tableId).set(table)
            .addOnSuccessListener {
                _table.value = table
                Logger.debug("Successfully init table -> table: $table")
            }
            .addOnFailureListener {
                Logger.debug("Table init failed -> table: $table")
            }
    }

    fun deal(gamePhase: GamePhase) {
        launch {
            if (_table.value.tableId.isNotBlank())
                when (gamePhase) {
                    GamePhase.DEAL -> dealHandCardsToPlayers(gamePhase)
                    else -> handleGamePhase(gamePhase)
                }
        }
    }

    private fun handleGamePhase(gamePhase: GamePhase) {
        phaseDependingCards.addAll(
            when (gamePhase) {
                GamePhase.FLOP -> getCardsAndHandleRemainingCardStack(gamePhase)
                GamePhase.TURN -> getCardsAndHandleRemainingCardStack(gamePhase)
                GamePhase.RIVER -> getCardsAndHandleRemainingCardStack(gamePhase)
                GamePhase.DEAL -> {
                    emptyList<Card>()
                }
                GamePhase.SHUFFLE -> {
                    emptyList<Card>()
                }
            }
        )
        _boardCards.value = phaseDependingCards
        updateGamePhase(gamePhase)
    }

    private fun getCardsAndHandleRemainingCardStack(gamePhase: GamePhase): List<Card> {
        val dealAmount = if (gamePhase == GamePhase.FLOP) 3 else 1
        val burnedRemainingCards = remainingCards.drop(1)
        val cardsToDeal = burnedRemainingCards.take(dealAmount)
        val postActionRemainingCards = burnedRemainingCards.drop(dealAmount)
        remainingCards = postActionRemainingCards
        return cardsToDeal
    }

    private fun dealHandCardsToPlayers(gamePhase: GamePhase) {
        _joinedPlayers.value.let { players ->
            if (players.none { player -> player.tableId.isBlank() }) {
                val cards = DeckHelper.getRandomCardsByPlayerCount(playerCount = players.count())
                _round.value++
                remainingCards = cards.toList()
                players.forEachIndexed { index, player ->
                    val currentHand = Hand(one = remainingCards.first(), two = remainingCards.second(), round = _round.value)
                    remainingCards = remainingCards.drop(2)
                    checkIfDealingCardsToPlayersHasAccomplished(remainingCards)
                    gamesCollectionRef.document(player.tableId).collection(COLLECTION_PLAYERS).document(player.uuid).collection(COLLECTION_HAND_CARDS).document("currentHand").set(currentHand)
                        .addOnSuccessListener {
                            Logger.debug("Spieler ${player.nickName} sollte jetzt ne hand haben")
                            if (index == players.size - 1) {
                                updateGamePhase(gamePhase = gamePhase)
                                launch { _onPlaySound.emit(Unit) }
                            }
                        }
                        .addOnFailureListener {
                            Logger.debug("Karten konnten nicht an Spieler ${player.nickName} verteilt werden")
                            return@addOnFailureListener
                        }
                }
            } else {
                launch { _onPlayerCountError.emit(PlayerCountError.NOT_ENOUGH) }
            }
        }
    }

    private fun checkIfDealingCardsToPlayersHasAccomplished(remainingCards: List<Card>) {
        launch {
            if (remainingCards.count() == BOARD_CARDS_COUNT) _onDealingCardsToPlayersAccomplished.emit(Unit)
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
        _table.value.tableId.let { tableId ->
            _joinedPlayers.value.let { players ->
                players.forEach { player ->
                    gamesCollectionRef.document(tableId).collection(COLLECTION_PLAYERS).document(player.uuid).collection(COLLECTION_HAND_CARDS).document(DOCUMENT_CURRENT_HAND).delete()
                        .addOnSuccessListener {
                            Logger.debug("currentHand of player ${player.nickName} is deleted")
                        }
                        .addOnFailureListener {
                            Logger.debug("failed deleting all player hand cards")
                        }
                }
            }
            Logger.debug("delete all player hand cards")
            _boardCards.value = defaultBoard
            phaseDependingCards.clear()
            remainingCards = emptyList()
        }
    }

    fun quitTable() {
        if (_table.value.tableId != "") {
            gamesCollectionRef.document(_table.value.tableId).delete()
                .addOnSuccessListener {
                    Logger.debug("tableId: ${_table.value.tableId} Spiel l??schen hat geklappt")
                    pokerTable = null
                    launch { _onNavigateBack.emit(Unit) }
                }
                .addOnFailureListener {
                    Logger.debug("tableId: ${_table.value.tableId} Spiel l??schen hat NICHT geklappt")
                    launch { _onShowErrorMessage.emit("Spiel beenden hat nicht geklappt") }
                }
        }
    }

    fun reset() {
        updateGamePhase(GamePhase.SHUFFLE)
    }
}

enum class GamePhase(val buttonText: String) {
    DEAL("Dealen"), FLOP("Flop"), TURN("Turn"), RIVER("River"), SHUFFLE("Shuffle")
}

enum class PlayerCountError(val errorMessage: String) {
    NOT_ENOUGH("Nicht genug Spieler"), TOO_MANY("Zu viele Spieler")
//    NOT_ENOUGH(R.string.player_count_error_not_enough_message), TOO_MANY(R.string.player_count_error_too_many_message), NONE(R.string.player_count_error_too_none)
}
