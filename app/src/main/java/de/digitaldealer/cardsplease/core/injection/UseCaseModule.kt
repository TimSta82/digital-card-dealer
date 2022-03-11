package de.digitaldealer.cardsplease.core.injection

import de.digitaldealer.cardsplease.domain.usecases.DrawAmountOfCardsUseCase
import de.digitaldealer.cardsplease.domain.usecases.GetNewDeckUseCase
import de.digitaldealer.cardsplease.domain.usecases.RefreshQuestionsFromApiUseCase
import de.digitaldealer.cardsplease.domain.usecases.WatchQuestionsFromDbUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { RefreshQuestionsFromApiUseCase() }
    factory { WatchQuestionsFromDbUseCase() }
    factory { GetNewDeckUseCase() }
    factory { DrawAmountOfCardsUseCase() }
}
