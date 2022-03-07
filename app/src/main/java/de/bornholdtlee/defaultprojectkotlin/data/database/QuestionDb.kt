package de.bornholdtlee.defaultprojectkotlin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.bornholdtlee.defaultprojectkotlin.data.database.dao.QuestionDao
import de.bornholdtlee.defaultprojectkotlin.data.database.model.QuestionEntity

@Database(entities = [QuestionEntity::class], version = 1)
abstract class QuestionDb : RoomDatabase() {

    companion object {
        const val DB_NAME = "questionDb"
    }

    abstract fun questionDao(): QuestionDao
}
