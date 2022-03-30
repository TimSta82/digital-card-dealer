package de.digitaldealer.cardsplease.ui.main.dealer_device

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.*
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.*
import de.digitaldealer.cardsplease.extensions.second
import de.digitaldealer.cardsplease.ui.extensions.launch
import de.digitaldealer.cardsplease.ui.util.SingleLiveEvent
import de.digitaldealer.cardsplease.ui.util.TableNames
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import java.util.*

class DealerViewModel : ViewModel(), KoinComponent {

    private val _table = MutableStateFlow(PokerTable())
    val table = _table.asStateFlow()

    private val _boardCards = MutableStateFlow(listOf(Card(), Card(), Card(), Card(), Card()))
    val boardCards = _boardCards.asStateFlow()

    private val _turn = MutableStateFlow(listOf(Card()))
    val turn = _turn.asStateFlow()

    private val _river = MutableStateFlow(listOf(Card()))
    val river = _river.asStateFlow()

    private val _onOpenAddPlayerDialog = MutableLiveData<PokerTable?>()
    val onOpenAddPlayerDialog: LiveData<PokerTable?> = _onOpenAddPlayerDialog

    private val _gamePhase = MutableLiveData(GamePhase.DEAL)
    val gamePhase: LiveData<GamePhase> = _gamePhase

    private val _joinedPlayers = MutableLiveData<List<Player>>()
    val joinedPlayers: LiveData<List<Player>> = _joinedPlayers

    private val _onPlayerCountError = SingleLiveEvent<PlayerCountError>()
    val onPlayerCountError: LiveData<PlayerCountError> = _onPlayerCountError

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

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private var playersListener: ListenerRegistration? = null
    private var remainingCards: List<Card> = emptyList()
    private var phaseDependingCards = mutableListOf<Card>()
    private var pokerTable: PokerTable? = null

    init {
        pokerTable = PokerTable(tableId = UUID.randomUUID().toString().take(12), tableName = TableNames.getRandomTableName())
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
                        _onPlayerCountError.value = PlayerCountError.NOT_ENOUGH
                        Logger.debug("Not enough players yet")
                    }
                    else -> {
                        _onPlayerCountError.value = PlayerCountError.TOO_MANY
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
        _joinedPlayers.value?.let { players ->
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

        }
    }

    private fun checkIfDealingCardsToPlayersHasAccomplished(remainingCards: List<Card>) {
        launch {
            if (remainingCards.count() == 8) _onDealingCardsToPlayersAccomplished.emit(Unit)
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
            _joinedPlayers.value?.let { players ->
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
            _boardCards.value = emptyList()
            phaseDependingCards.clear()
            remainingCards = emptyList()
        }
    }

    fun quitTable() {
        if (_table.value.tableId != "") {
            gamesCollectionRef.document(_table.value.tableId).delete()
                .addOnSuccessListener {
                    Logger.debug("tableId: ${_table.value.tableId} Spiel löschen hat geklappt")
                    pokerTable = null
                    launch { _onNavigateBack.emit(Unit) }
                }
                .addOnFailureListener {
                    Logger.debug("tableId: ${_table.value.tableId} Spiel löschen hat NICHT geklappt")
                    launch { _onShowErrorMessage.emit("Spiel beenden hat nicht geklappt") }
                }
        }
    }

    fun addPlayer() {
        _onOpenAddPlayerDialog.value = _table.value
    }

    fun reset() {
        updateGamePhase(GamePhase.SHUFFLE)
    }

    fun resetPlayerDeckId() {
        _onOpenAddPlayerDialog.value = null
    }
}

enum class GamePhase(val buttonText: String) {
    DEAL("Dealen"), FLOP("Flop"), TURN("Turn"), RIVER("River"), SHUFFLE("Shuffle")
}

enum class PlayerCountError(val errorMessageId: Int) {
    NOT_ENOUGH(R.string.player_count_error_not_enough_message), TOO_MANY(R.string.player_count_error_too_many_message)
}
