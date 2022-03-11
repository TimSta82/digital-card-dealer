package de.digitaldealer.cardsplease.core.injection

import de.digitaldealer.cardsplease.domain.usecases.*
import org.koin.dsl.module

val useCaseModule = module {

    factory { RefreshQuestionsFromApiUseCase() }
    factory { WatchQuestionsFromDbUseCase() }
    factory { GetNewDeckUseCase() }
    factory { DrawAmountOfCardsUseCase() }
    factory { ShuffleDeckUseCase() }
}
