package de.bornholdtlee.defaultprojectkotlin.core.injection

import androidx.room.Room
import de.bornholdtlee.defaultprojectkotlin.data.database.KeyValueStore
import de.bornholdtlee.defaultprojectkotlin.data.database.KeyValueStoreEncrypted
import de.bornholdtlee.defaultprojectkotlin.data.database.MIGRATION_1_2
import de.bornholdtlee.defaultprojectkotlin.data.database.QuestionDb
import de.bornholdtlee.defaultprojectkotlin.data.database.QuestionDb.Companion.DB_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), QuestionDb::class.java, DB_NAME)
            .fallbackToDestructiveMigration() // TODO: REMOVE DESTRUCTIVE FALLBACK BEFORE SHIPPING TO PROD!!!
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single { get<QuestionDb>().questionDao() }

    single { KeyValueStore(androidContext()) }
    single { KeyValueStoreEncrypted(androidContext()) }
}
