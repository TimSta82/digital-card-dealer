package de.bornholdtlee.defaultprojectkotlin.domain.usecases

import de.bornholdtlee.defaultprojectkotlin.data.database.model.QuestionEntity
import de.bornholdtlee.defaultprojectkotlin.data.repositories.QuestionRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

class WatchQuestionsFromDbUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

    fun call(): Flow<List<QuestionEntity>> = questionRepository.watchAllQuestions()
}
