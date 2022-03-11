package de.digitaldealer.cardsplease.data.repositories

import de.digitaldealer.cardsplease.data.database.dao.QuestionDao
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
