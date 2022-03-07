package de.bornholdtlee.defaultprojectkotlin.data.model

import com.google.gson.annotations.SerializedName
import de.bornholdtlee.defaultprojectkotlin.data.database.model.QuestionEntity

data class QuestionListDto(

    @SerializedName("items")
    val items: MutableList<QuestionDTO>

) {
    fun toQuestionEntityList(): List<QuestionEntity> = items.map(::QuestionEntity)
}
