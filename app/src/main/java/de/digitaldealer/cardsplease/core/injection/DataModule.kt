package de.digitaldealer.cardsplease.core.injection

import androidx.room.Room
import de.digitaldealer.cardsplease.data.database.KeyValueStore
import de.digitaldealer.cardsplease.data.database.KeyValueStoreEncrypted
import de.digitaldealer.cardsplease.data.database.MIGRATION_1_2
import de.digitaldealer.cardsplease.data.database.QuestionDb
import de.digitaldealer.cardsplease.data.database.QuestionDb.Companion.DB_NAME
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
