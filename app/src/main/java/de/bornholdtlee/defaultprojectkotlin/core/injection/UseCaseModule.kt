package de.bornholdtlee.defaultprojectkotlin.core.injection

import de.bornholdtlee.defaultprojectkotlin.domain.usecases.DrawAmountOfCardsUseCase
import de.bornholdtlee.defaultprojectkotlin.domain.usecases.GetNewDeckUseCase
import de.bornholdtlee.defaultprojectkotlin.domain.usecases.RefreshQuestionsFromApiUseCase
import de.bornholdtlee.defaultprojectkotlin.domain.usecases.WatchQuestionsFromDbUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { RefreshQuestionsFromApiUseCase() }
    factory { WatchQuestionsFromDbUseCase() }
    factory { GetNewDeckUseCase() }
    factory { DrawAmountOfCardsUseCase() }
}
