package de.bornholdtlee.defaultprojectkotlin.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import de.bornholdtlee.defaultprojectkotlin.data.database.model.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class QuestionDao : BaseDao<QuestionEntity>() {

    @Query("SELECT * FROM questions")
    abstract fun watchAll(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE question_id = :questionId")
    abstract fun watchQuestionById(questionId: String): Flow<QuestionEntity>

    @Query("DELETE FROM questions")
    abstract suspend fun removeAll()
}
