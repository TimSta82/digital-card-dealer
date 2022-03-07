package de.bornholdtlee.defaultprojectkotlin.core.injection

import de.bornholdtlee.defaultprojectkotlin.data.repositories.QuestionRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { QuestionRepository() }
}
