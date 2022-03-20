package de.digitaldealer.cardsplease.ui.main.player_device.insert_name

import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.COLLECTION_PLAYERS
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.extensions.second
import org.koin.core.component.KoinComponent
import java.util.*

class InsertNameViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

    val deckIdWithTableName = liveData { emit(getDeckIdAndTableName(savedState.get<String>("deckId") ?: "-1")) }

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    fun submitToGame(nickName: String) {
        deckIdWithTableName.value?.let { deckTable ->
            val player = Player(deckId = deckTable.first, tableName = deckTable.second, nickName = nickName, uuid = UUID.randomUUID().toString())
            loadGameByDeckIdFromFireStore(player = player)
        }
    }

    private fun loadGameByDeckIdFromFireStore(player: Player) {
        gamesCollectionRef.document(player.deckId).collection(COLLECTION_PLAYERS).document(player.uuid).set(player)
            .addOnSuccessListener {
                Logger.debug("Bingo, Spieler wurde dem Game hinzugefügt")
                _player.value = player
            }
            .addOnFailureListener {
                Logger.debug("Zonk, spieler hinzufügen hat nicht geklappt")
            }
    }

    private fun getDeckIdAndTableName(navArgument: String): Pair<String, String> {
        val codes = navArgument.split(":::")
        val deckId = codes.first()
        val tableName = codes.second()
        return Pair(deckId, tableName)
    }
}
