package de.digitaldealer.cardsplease.ui.main.satellite_device

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import de.digitaldealer.cardsplease.KEY_PLAYER_DECK_ID
import de.digitaldealer.cardsplease.KEY_PLAYER_NICK_NAME
import de.digitaldealer.cardsplease.core.utils.Logger
import org.koin.core.component.KoinComponent

class SatelliteViewModel : ViewModel(), KoinComponent {
    private val db = FirebaseFirestore.getInstance()
    private val playersRef = db.collection("players").document("player")

    fun submitToGame(deckId: String, nickName: String) {
        val playerMap = hashMapOf<String, Any>()

        playerMap[KEY_PLAYER_NICK_NAME] = nickName
        playerMap[KEY_PLAYER_DECK_ID] = deckId
        playersRef.set(playerMap)
            .addOnSuccessListener {
                Logger.debug("Player upload successful")
            }
            .addOnFailureListener {
                Logger.debug("Player upload failed")
            }
    }
}

data class Player(
    val deckId: String,
    val nickName: String
)
