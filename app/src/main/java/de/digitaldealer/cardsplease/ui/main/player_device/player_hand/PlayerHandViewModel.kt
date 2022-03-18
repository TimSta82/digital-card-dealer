package de.digitaldealer.cardsplease.ui.main.player_device.player_hand

import androidx.lifecycle.*
import org.koin.core.component.KoinComponent

class PlayerHandViewModel(savedState: SavedStateHandle) : ViewModel(), KoinComponent {

//    val deckId = liveData { emit(savedState.get<String>("deckId") ?: "-1") }
//    val nickname = liveData { emit(savedState.get<String>("nickName") ?: "-1") }
//
    private val _d = MutableLiveData<String>()
    val d: LiveData<String> = _d

    private val _n = MutableLiveData<String>()
    val n: LiveData<String> = _n

    init {
        // TODO find out why this gets called endless
        _d.value = savedState.get<String>("deckId") ?: "-1"
        _n.value = savedState.get<String>("nickName") ?: "-1"
//        Logger.debug("deckId: ${deckId.value}")
//        Logger.debug("name: ${nickname.value}")
    }
}