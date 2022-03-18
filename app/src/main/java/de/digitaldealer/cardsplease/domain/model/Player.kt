package de.digitaldealer.cardsplease.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    @PropertyName("deckId")
    val deckId: String,
    @PropertyName("nickName")
    val nickName: String,
) : Parcelable {
    constructor() : this(
        deckId = "-1",
        nickName = ""
    )
}