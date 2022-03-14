package de.digitaldealer.cardsplease.ui.main.satellite_device

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Game
import de.digitaldealer.cardsplease.domain.model.Deck
import de.digitaldealer.cardsplease.domain.model.Player
import org.koin.core.component.KoinComponent

class SatelliteViewModel : ViewModel(), KoinComponent {
    private val db = FirebaseFirestore.getInstance()
    private val playersRef = db.collection("players").document("player")
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    fun submitToGame(deckId: String, nickName: String) {
        loadGameByDeckIdFromFireStore(deckId, nickName)
    }

    private fun loadGameByDeckIdFromFireStore(deckId: String, nickName: String) {
        gamesCollectionRef.document(deckId).get()
            .addOnSuccessListener { snapshot ->
                val game = snapshot.toObject<Game>()
                val alreadyRegisteredPlayers = game?.players ?: emptyList()
                val enrichedPlayersList = alreadyRegisteredPlayers.toMutableList()
                enrichedPlayersList.add(Player(deckId = deckId, nickName = nickName))
                game?.let { game ->
                    game.deck?.let { deck ->
                        updatePlayers(deck, enrichedPlayersList)
                    } ?: Logger.debug("Error: No deck found")
                } ?: Logger.debug("Error: No game found")
            }
            .addOnFailureListener {
                Logger.debug("error: $it")
            }
    }

    private fun updatePlayers(deck: Deck, enrichedPlayersList: MutableList<Player>) {
        gamesCollectionRef.document(deck.deckId).set(Game(deck = deck, players = enrichedPlayersList))
            .addOnSuccessListener { Logger.debug("Whooo hooo") }
            .addOnFailureListener { Logger.debug("Oooooohh nooooooo") }
    }
}
