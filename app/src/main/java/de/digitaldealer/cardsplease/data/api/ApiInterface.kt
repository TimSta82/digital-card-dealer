package de.digitaldealer.cardsplease.data.api

import de.digitaldealer.cardsplease.data.model.CardsResponseDTO
import de.digitaldealer.cardsplease.data.model.DeckResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/api/deck/new")
    suspend fun getNewDeck(): Response<DeckResponseDTO>

    @GET("/api/deck/{deck_id}/shuffle")
    suspend fun shuffleDeck(@Path("deck_id") deckId: String): Response<DeckResponseDTO>

    @GET("/api/deck/{deck_id}/draw")
    suspend fun drawAmountOfCards(@Path("deck_id") deckId: String, @Query("count") count: Int): Response<CardsResponseDTO>

}
