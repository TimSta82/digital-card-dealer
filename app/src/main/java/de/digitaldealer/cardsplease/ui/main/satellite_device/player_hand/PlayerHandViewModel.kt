package de.digitaldealer.cardsplease.ui.main.satellite_device.player_hand

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import de.digitaldealer.cardsplease.COLLECTION_GAMES
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.domain.model.Deck
import de.digitaldealer.cardsplease.domain.model.Game
import de.digitaldealer.cardsplease.domain.model.Player
import org.koin.core.component.KoinComponent

class PlayerHandViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

    val deckId = liveData { emit(savedState.get<String>("deckId") ?: "-1") }
    val nickname = liveData { emit(savedState.get<String>("nickName") ?: "-1") }

}