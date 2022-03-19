package de.digitaldealer.cardsplease.ui.main.player_device.player_hand

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.COLLECTION_HAND_CARDS
import de.digitaldealer.cardsplease.COLLECTION_PLAYERS
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.domain.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class PlayerHandViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

    private val _player = MutableStateFlow(savedState.get<Player>("player") ?: Player())
    val player = _player.asStateFlow()

    private val _currentHand = MutableStateFlow(Hand())
    val currentHand = _currentHand.asStateFlow()

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private var playerHandListener: ListenerRegistration? = null

    private fun isHandValid(hand: Hand): Boolean = hand.one.value != "" && hand.two.value != ""

    fun onStop() {
        playerHandListener?.remove()
    }

    fun onStart() {
        playerHandListener?.remove()
        playerHandListener = gamesCollectionRef.document(_player.value.deckId).collection(COLLECTION_PLAYERS).document(_player.value.nickName).collection(COLLECTION_HAND_CARDS)
            .addSnapshotListener { snapshot, error ->
                if (snapshot?.isEmpty?.not() == true) {
                    val hands = ArrayList<Hand>()
                    for (doc in snapshot) {
                        val hand = doc.toObject<Hand>()
                        if (isHandValid(hand)) hands.add(hand)
                    }
                    Logger.debug("currentHand: $hands")
                    _currentHand.value = hands.first()
                }
                if (error != null) {
                    Logger.debug("Loading player failed")
                    return@addSnapshotListener
                }
            }
    }
}