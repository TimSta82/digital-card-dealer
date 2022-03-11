package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.repositories.QuestionRepository
import org.koin.core.component.inject

class WatchQuestionsFromDbUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

//    fun call(): Flow<List<QuestionEntity>> = questionRepository.watchAllQuestions()
}
