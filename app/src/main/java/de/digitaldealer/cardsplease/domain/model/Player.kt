package de.digitaldealer.cardsplease.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    @PropertyName("tableId")
    val tableId: String,
    @PropertyName("tableName")
    val tableName: String,
    @PropertyName("nickName")
    val nickName: String,
    @PropertyName("uuid")
    val uuid: String
) : Parcelable {
    constructor() : this(
        tableId = "-1",
        nickName = "",
        tableName = "",
        uuid = ""
    )
}
