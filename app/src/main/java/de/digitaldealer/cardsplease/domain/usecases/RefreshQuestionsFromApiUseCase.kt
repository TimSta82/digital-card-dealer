package de.digitaldealer.cardsplease.domain.usecases

import de.digitaldealer.cardsplease.data.repositories.QuestionRepository
import org.koin.core.component.inject

class RefreshQuestionsFromApiUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

//    suspend fun call() = simpleResponseCall(questionRepository.startDownload()) { questionListDto -> questionRepository.saveQuestions(questionListDto) }
}
