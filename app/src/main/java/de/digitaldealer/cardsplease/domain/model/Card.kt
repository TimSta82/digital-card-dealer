package de.digitaldealer.cardsplease.domain.model

import com.google.firebase.firestore.PropertyName

data class Card(
    @PropertyName("image")
    val image: String,
    @PropertyName("value")
    val value: String,
    @PropertyName("suit")
    val suit: String,
    @PropertyName("code")
    val code: String
) {
    constructor() : this(
        image = "",
        value = "",
        suit = "",
        code = ""
    )
}
