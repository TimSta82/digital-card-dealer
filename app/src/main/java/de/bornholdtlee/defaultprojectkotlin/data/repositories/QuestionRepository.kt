package de.bornholdtlee.defaultprojectkotlin.data.repositories

import de.bornholdtlee.defaultprojectkotlin.data.api.ResponseEvaluator
import de.bornholdtlee.defaultprojectkotlin.data.database.dao.QuestionDao
import de.bornholdtlee.defaultprojectkotlin.data.database.model.QuestionEntity
import de.bornholdtlee.defaultprojectkotlin.data.model.QuestionListDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class QuestionRepository : BaseRepository() {

    private val questionDao by inject<QuestionDao>()

//    suspend fun startDownload(): ResponseEvaluator.Result<QuestionListDto> = apiCall { api.loadQuestions("android") }
//
//    fun watchAllQuestions(): Flow<List<QuestionEntity>> = questionDao.watchAll()
//
//    fun saveQuestions(questionListDto: QuestionListDto) {
//        repositoryScope.launch {
//            questionDao.insert(questionListDto.toQuestionEntityList())
//        }
//    }
}
