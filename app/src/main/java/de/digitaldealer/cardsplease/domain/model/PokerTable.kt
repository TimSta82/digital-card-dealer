package de.digitaldealer.cardsplease.domain.model

import com.google.firebase.firestore.PropertyName

data class PokerTable(
    @PropertyName("tableId")
    val tableId: String,
    @PropertyName("tableName")
    val tableName: String,
) {
    constructor() : this(
        tableId = "", tableName = ""
    )
}
