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
import de.digitaldealer.cardsplease.domain.usecases.DeletePlayerLocallyUseCase
import de.digitaldealer.cardsplease.ui.NavigationRoutes.NAV_ARG_PLAYER
import de.digitaldealer.cardsplease.ui.extensions.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerHandViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

    private val deletePlayerLocallyUseCase by inject<DeletePlayerLocallyUseCase>()

    private val _player = MutableStateFlow(savedState.get<Player>(NAV_ARG_PLAYER) ?: Player())
    val player = _player.asStateFlow()

    private val _currentHand = MutableStateFlow(Hand())
    val currentHand = _currentHand.asStateFlow()

    private val _onLeaveTable = MutableSharedFlow<Unit>()
    val onLeaveTable = _onLeaveTable.asSharedFlow()

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private var playerHandListener: ListenerRegistration? = null

    private fun isHandValid(hand: Hand): Boolean = hand.one.value != "" && hand.two.value != ""

    fun onStop() {
        playerHandListener?.remove()
    }

    fun onStart() {
        playerHandListener?.remove()
        playerHandListener = gamesCollectionRef.document(_player.value.tableId).collection(COLLECTION_PLAYERS).document(_player.value.uuid).collection(COLLECTION_HAND_CARDS)
            .addSnapshotListener { snapshot, error ->
                if (snapshot?.isEmpty?.not() == true) {
                    val hands = ArrayList<Hand>()
                    for (doc in snapshot) {
                        val hand = doc.toObject<Hand>()
                        if (isHandValid(hand)) hands.add(hand)
                    }
                    Logger.debug("currentHand: $hands")
                    _currentHand.value = hands.first()
                } else {
                    _currentHand.value = Hand()
                }
                if (error != null) {
                    Logger.debug("Loading player failed")
                    _currentHand.value = Hand()
                    return@addSnapshotListener
                }
            }
    }

    fun disconnectPlayer() {
        gamesCollectionRef.document(_player.value.tableId).collection(COLLECTION_PLAYERS).document(_player.value.uuid).delete()
            .addOnSuccessListener {
                Logger.debug("Tschüss ${_player.value.nickName}")
                launch {
                    deletePlayerLocallyUseCase.call()
                    _onLeaveTable.emit(Unit)
                }
            }
            .addOnFailureListener {
                Logger.debug("Du musst weiter spielen, weil abmelden hat nicht geklappt")
            }
    }
}
