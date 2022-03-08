package de.bornholdtlee.defaultprojectkotlin.data.api

import de.bornholdtlee.defaultprojectkotlin.data.model.CardsResponseDTO
import de.bornholdtlee.defaultprojectkotlin.data.model.DeckResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/api/deck/new/shuffle/?deck_count=1")
    suspend fun getNewDeck(): Response<DeckResponseDTO>

    @GET("/api/deck/{deck_id}/draw/?count=2")
    suspend fun drawAmountOfCards(@Path("deck_id") deckId: String, @Query("count") count: Int): Response<CardsResponseDTO>

}
