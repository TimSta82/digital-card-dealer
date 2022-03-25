package de.digitaldealer.cardsplease.core.injection

import de.digitaldealer.cardsplease.data.repositories.QuestionRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { QuestionRepository() }
    factory { DeckRepository() }
}
