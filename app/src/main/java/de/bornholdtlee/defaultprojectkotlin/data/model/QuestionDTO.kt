package de.bornholdtlee.defaultprojectkotlin.data.model

import com.google.gson.annotations.SerializedName

data class QuestionDTO(

    @SerializedName("question_id")
    val questionId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("link")
    val link: String
)
