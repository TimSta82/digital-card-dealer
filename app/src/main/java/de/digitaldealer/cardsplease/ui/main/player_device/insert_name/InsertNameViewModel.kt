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
import de.digitaldealer.cardsplease.domain.model.Deck
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.extensions.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import java.util.*

class InsertNameViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _onNavigateBack = MutableSharedFlow<Boolean>()
    val onNavigateBack = _onNavigateBack.asSharedFlow()

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    private val _deckFromFirestore = MutableStateFlow(Deck())
    val deckFromFireStore = _deckFromFirestore.asStateFlow()

    init {
        checkIfDeckExists()
    }

    private fun checkIfDeckExists() {
        val deckId = savedState.get<String>("deckId") ?: "-1"
        if (deckId != "-1") {
            gamesCollectionRef.document(deckId).get()
                .addOnSuccessListener { snapshot ->
                    Logger.debug("snapshot: $snapshot")
                    if (snapshot?.exists() == true) {
                        val deck = snapshot.toObject<Deck>()
                        Logger.debug("deck? = $deck")
                        deck?.let {
                            Logger.debug("deck = $it")
                            _deckFromFirestore.value = it
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
        _deckFromFirestore.value.let { deck ->
            if (deck.deckId != "") {
                val player = Player(deckId = deck.deckId, tableName = deck.tableName, nickName = nickName, uuid = UUID.randomUUID().toString())
                addPlayerToGameAtFireStore(player = player)
            } else launch { _onNavigateBack.emit(true) }
        }
    }

    private fun addPlayerToGameAtFireStore(player: Player) {
        _isLoading.value = true
        gamesCollectionRef.document(player.deckId).collection(COLLECTION_PLAYERS).document(player.uuid).set(player)
            .addOnSuccessListener {
                Logger.debug("Bingo, Spieler wurde dem Game hinzugefügt")
                _isLoading.value = false
                _player.value = player
            }
            .addOnFailureListener {
                _isLoading.value = false
                 launch { _onNavigateBack.emit(true) }
                Logger.debug("Zonk, spieler hinzufügen hat nicht geklappt")
            }
    }
}
