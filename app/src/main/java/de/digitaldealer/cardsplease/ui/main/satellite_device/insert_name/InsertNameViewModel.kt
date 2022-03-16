package de.digitaldealer.cardsplease.ui.main.satellite_device.insert_name

import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Deck
import de.digitaldealer.cardsplease.domain.model.Game
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
        gamesCollectionRef.document(deckId).get()
            .addOnSuccessListener { snapshot ->
                val game = snapshot.toObject<Game>()
                val alreadyRegisteredPlayers = game?.players ?: emptyList()
                val enrichedPlayersList = alreadyRegisteredPlayers.toMutableList()
                val newPlayer = Player(deckId = deckId, nickName = nickName)
                enrichedPlayersList.add(newPlayer)
                game?.let { game ->
                    game.deck?.let { deck ->
                        updatePlayers(deck, enrichedPlayersList, newPlayer)
                    } ?: Logger.debug("Error: No deck found")
                } ?: Logger.debug("Error: No game found")
            }
            .addOnFailureListener {
                Logger.debug("error: $it")
            }
    }

    private fun updatePlayers(deck: Deck, enrichedPlayersList: MutableList<Player>, player: Player) {
        gamesCollectionRef.document(deck.deckId).set(Game(deck = deck, players = enrichedPlayersList))
            .addOnSuccessListener {
                Logger.debug("Whooo hooo")
                _player.value = player
            }
            .addOnFailureListener { Logger.debug("Oooooohh nooooooo") }
    }
}