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
import de.digitaldealer.cardsplease.ui.main.dealer_device.Hand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class PlayerHandViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {

//    val deckId = liveData { emit(savedState.get<String>("deckId") ?: "-1") }
//    val nickname = liveData { emit(savedState.get<String>("nickName") ?: "-1") }
//
//    private val _d = MutableLiveData<String>()
//    val d: LiveData<String> = _d
//
//    private val _n = MutableLiveData<String>()
//    val n: LiveData<String> = _n

    /**
    VM
    private val _articles = MutableStateFlow(emptyList<Content.Article>())
    val articles = _articles.asStateFlow()
    val articles: StateFlow<List<Content.Article>> = runtimeDataController.articles

    screen
    val followUpArticle: Content.Article? by remember {
    val allArticles: List<Content.Article> = contentViewModel.articles.value }

     */

    private val _deckId = MutableStateFlow(savedState.get<String>("deckId") ?: "-1")
    val deckId = _deckId.asStateFlow()

    private val _nickname = MutableStateFlow(savedState.get<String>("nickName") ?: "-1")
    val nickname = _nickname.asStateFlow()

    private val _currentHand = MutableStateFlow(Hand.getDefaultHand())
    val currentHand = _currentHand.asStateFlow()

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)

    private var playerHandListener: ListenerRegistration? = null

    fun onStart() {
        // TODO find out why this gets called endless
//        _d.value = savedState.get<String>("deckId") ?: "-1"
//        _n.value = savedState.get<String>("nickName") ?: "-1"
        Logger.debug("deckId: ${deckId.value}")
        Logger.debug("name: ${nickname.value}")
        playerHandListener?.remove()
        playerHandListener = gamesCollectionRef.document(deckId.value!!).collection(COLLECTION_PLAYERS).document(nickname.value!!).collection(COLLECTION_HAND_CARDS)
            .addSnapshotListener { snapshot, error ->
                if (snapshot?.isEmpty?.not() == true) {
                    val hands = ArrayList<Hand>()
                    for (doc in snapshot) {
                        val hand = doc.toObject<Hand>()
                        hands.add(hand)
                    }
                    Logger.debug("currentHand: $hands")
                    _currentHand.value = hands.first()
                }
                if (error != null) {
                    Logger.debug("Loading player failed")
                }
            }
    }

    fun onStop() {
        playerHandListener?.remove()
    }
}