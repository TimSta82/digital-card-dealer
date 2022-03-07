package de.bornholdtlee.defaultprojectkotlin.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Question ADD COLUMN description TEXT NOT NULL DEFAULT ''")
    }
}
