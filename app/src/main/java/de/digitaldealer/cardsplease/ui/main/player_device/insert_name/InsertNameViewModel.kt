package de.digitaldealer.cardsplease.ui.main.player_device.insert_name

import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.COLLECTION_PLAYERS
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Player
import org.koin.core.component.KoinComponent

class InsertNameViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

    val deckId = liveData { emit(savedState.get<String>("deckId") ?: "-1") }

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    fun submitToGame(nickName: String) {
        loadGameByDeckIdFromFireStore(deckId = savedState.get<String>("deckId") ?: "-1", nickName = nickName)
    }

    private fun loadGameByDeckIdFromFireStore(deckId: String, nickName: String) {
        val player = Player(deckId = deckId, nickName = nickName)
        gamesCollectionRef.document(deckId).collection(COLLECTION_PLAYERS).document(nickName).set(player)
            .addOnSuccessListener {
                Logger.debug("Bingo, Spieler wurde dem Game hinzugefügt")
                _player.value = player
            }
            .addOnFailureListener {
                Logger.debug("Zonk, spieler hinzufügen hat nicht geklappt")
            }
    }
}