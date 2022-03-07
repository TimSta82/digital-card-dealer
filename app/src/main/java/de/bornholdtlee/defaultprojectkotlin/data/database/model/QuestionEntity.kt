package de.bornholdtlee.defaultprojectkotlin.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.bornholdtlee.defaultprojectkotlin.data.model.QuestionDTO

@Entity(tableName = "questions")
data class QuestionEntity(

    @PrimaryKey
    @ColumnInfo(name = "question_id")
    var questionId: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "link")
    var link: String
) {
    constructor(dto: QuestionDTO) : this(
        questionId = dto.questionId,
        title = dto.title,
        link = dto.link
    )
}
