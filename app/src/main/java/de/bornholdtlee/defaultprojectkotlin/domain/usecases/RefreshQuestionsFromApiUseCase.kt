package de.bornholdtlee.defaultprojectkotlin.domain.usecases

import de.bornholdtlee.defaultprojectkotlin.data.repositories.QuestionRepository
import org.koin.core.component.inject

class RefreshQuestionsFromApiUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

//    suspend fun call() = simpleResponseCall(questionRepository.startDownload()) { questionListDto -> questionRepository.saveQuestions(questionListDto) }
}
