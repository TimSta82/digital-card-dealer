package de.digitaldealer.cardsplease.domain.model

import com.google.firebase.firestore.PropertyName

data class Hand(
    @PropertyName("one")
    val one: Card,
    @PropertyName("two")
    val two: Card,
    @PropertyName("round")
    val round: Int
) {
    constructor() : this(
        one = Card(),
        two = Card(),
        round = 0
    )

    fun isValid(): Boolean = one.value.isNotBlank() && two.value.isNotBlank()
}
