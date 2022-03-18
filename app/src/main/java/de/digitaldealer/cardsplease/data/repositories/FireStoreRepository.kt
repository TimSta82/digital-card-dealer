package de.digitaldealer.cardsplease.data.repositories

import com.google.firebase.firestore.FirebaseFirestore

class FireStoreRepository {

    //    private val gamesDeckRef = db.collection(COLLECTION_GAMES).document("deckId")
//    private val playersRef = db.collection("players").document("player")

    companion object {
        const val COLLECTION_GAMES = "games"
    }

    private val db = FirebaseFirestore.getInstance()
    private val gamesCollectionRef = db.collection(COLLECTION_GAMES)


//    fun initGameWithDeckId(deck: Deck) {
////        val city = City("Los Angeles", "CA", "USA",
////            false, 5000000L, listOf("west_coast", "socal"))
////        db.collection("cities").document("LA").set(city)
//        gamesCollectionRef.document(deck.deckId).set(deck)
//            .addOnSuccessListener {
////                _onUploadDeckSuccessful.call()
//                Logger.debug("Successfully uploaded -> deckId: ${deck.deckId}")
//            }
//            .addOnFailureListener {
//                Logger.debug("Uploading failed -> deckId: ${deck.deckId}")
//            }
//    }
//
//    fun getGameWithPlayersByDeckId(deckId: String) {
//        gamesCollectionRef.document(deckId).get()
//            .addOnSuccessListener { snapshot ->
//                val game = snapshot.toObject<Game>()
//                game?.let { FireStoreResult.Success(it) } ?: FireStoreResult.Failure("snapshot does not exist")
//            }
//            .addOnFailureListener { exception ->
//                FireStoreResult.Failure(exception.toString())
//            }
//    }
}


