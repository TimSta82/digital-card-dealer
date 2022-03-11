package de.digitaldealer.cardsplease.data.model

import com.google.gson.annotations.SerializedName
import de.digitaldealer.cardsplease.data.database.model.QuestionEntity

data class QuestionListDto(

    @SerializedName("items")
    val items: MutableList<QuestionDTO>

) {
    fun toQuestionEntityList(): List<QuestionEntity> = items.map(::QuestionEntity)
}
