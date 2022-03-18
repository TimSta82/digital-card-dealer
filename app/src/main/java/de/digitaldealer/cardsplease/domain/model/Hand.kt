package de.digitaldealer.cardsplease.domain.model

import com.google.firebase.firestore.PropertyName

data class Hand(
    @PropertyName("one")
    val one: Card,
    @PropertyName("two")
    val two: Card
) {
    constructor() : this(
        one = Card(),
        two = Card()
    )
}