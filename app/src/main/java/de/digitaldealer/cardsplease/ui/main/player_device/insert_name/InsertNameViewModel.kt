package de.digitaldealer.cardsplease.ui.main.player_device.insert_name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.COLLECTION_PLAYERS
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.domain.model.PokerTable
import de.digitaldealer.cardsplease.domain.usecases.SetPlayerLocallyUseCase
import de.digitaldealer.cardsplease.domain.usecases.WatchHasInternetAccessUseCase
import de.digitaldealer.cardsplease.ui.NavigationRoutes.NAV_ARG_TABLE_ID
import de.digitaldealer.cardsplease.ui.extensions.launch
import de.digitaldealer.cardsplease.ui.extensions.stateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class InsertNameViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

    private val setPlayerLocallyUseCase by inject<SetPlayerLocallyUseCase>()
    private val watchHasInternetAccessUseCase by inject<WatchHasInternetAccessUseCase>()
    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _onNavigateBack = MutableSharedFlow<Boolean>()
    val onNavigateBack = _onNavigateBack.asSharedFlow()

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    private val _tableFromFirestore = MutableStateFlow(PokerTable())
    val tableFromFireStore = _tableFromFirestore.asStateFlow()

    val hasInternet = stateFlow(flow = watchHasInternetAccessUseCase.call(), initialValue = true)

    init {
        checkIfDeckExists()
    }

    private fun checkIfDeckExists() {
        val tableId = savedState.get<String>(NAV_ARG_TABLE_ID) ?: "-1"
        if (tableId != "-1") {
            gamesCollectionRef.document(tableId).get()
                .addOnSuccessListener { snapshot ->
                    Logger.debug("snapshot: $snapshot")
                    if (snapshot?.exists() == true) {
                        val pokerTable = snapshot.toObject<PokerTable>()
                        Logger.debug("pokerTable? = $pokerTable")
                        pokerTable?.let {
                            Logger.debug("pokerTable = $it")
                            _tableFromFirestore.value = it
                        } ?: launch { _onNavigateBack.emit(true) }
                    }
                }
                .addOnFailureListener {
                    Logger.debug("exception: $it")
                    launch { _onNavigateBack.emit(true) }
                }
        }
    }

    fun submitToGame(nickName: String) {
        _tableFromFirestore.value.let { table ->
            if (table.tableId != "") {
                val player = Player(tableId = table.tableId, tableName = table.tableName, nickName = nickName, uuid = UUID.randomUUID().toString())
                addPlayerToGameAtFireStore(player = player)
            } else launch { _onNavigateBack.emit(true) }
        }
    }

    private fun addPlayerToGameAtFireStore(player: Player) {
        _isLoading.value = true
        gamesCollectionRef.document(player.tableId).collection(COLLECTION_PLAYERS).document(player.uuid).set(player)
            .addOnSuccessListener {
                Logger.debug("Bingo, Spieler wurde dem Game hinzugefügt")
                _isLoading.value = false
                _player.value = player
                savePlayerLocally(player)
            }
            .addOnFailureListener {
                _isLoading.value = false
                launch { _onNavigateBack.emit(true) }
                Logger.debug("Zonk, spieler hinzufügen hat nicht geklappt")
            }
    }

    private fun savePlayerLocally(player: Player) {
        launch {
            setPlayerLocallyUseCase.call(player = player)
        }
    }
}
