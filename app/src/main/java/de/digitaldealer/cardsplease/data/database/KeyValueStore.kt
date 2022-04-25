package de.digitaldealer.cardsplease.data.database

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import de.digitaldealer.cardsplease.data.database.KeyValueStore.Keys.APP_DATA_STORE_NAME
import de.digitaldealer.cardsplease.data.database.KeyValueStore.Keys.KEY_PLAYER_NICK_NAME
import de.digitaldealer.cardsplease.data.database.KeyValueStore.Keys.KEY_PLAYER_TABLE_ID
import de.digitaldealer.cardsplease.data.database.KeyValueStore.Keys.KEY_PLAYER_TABLE_NAME
import de.digitaldealer.cardsplease.data.database.KeyValueStore.Keys.KEY_PLAYER_UUID
import de.digitaldealer.cardsplease.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * LEGACY INFO: When migrating from existing Key Value Store:
 *
 *     private val Context.dataStore by preferencesDataStore(
 *          name = context.getString(R.string.app_name),
 *          produceMigrations = {
 *              listOf(
 *                  SharedPreferencesMigration(
 *                      context = context,
 *                      sharedPreferencesName = context.getString(R.string.app_name)
 *                  )
 *              )
 *          }
 *      )
 */
class KeyValueStore(private val context: Context) {

    private object Keys {
        const val APP_DATA_STORE_NAME = "REPLACE_MY_NAME"
        val EXAMPLE_BOOLEAN = booleanPreferencesKey("EXAMPLE_BOOLEAN")
        val EXAMPLE_DOUBLE = doublePreferencesKey("EXAMPLE_DOUBLE")
        val EXAMPLE_STRING_SET = stringSetPreferencesKey("EXAMPLE_STRING_SET")

        //        val KEY_PLAYER = stringPreferencesKey("KEY_PLAYER")
        val KEY_PLAYER_UUID = stringPreferencesKey("KEY_PLAYER_UUID")
        val KEY_PLAYER_NICK_NAME = stringPreferencesKey("KEY_PLAYER_NICK_NAME")
        val KEY_PLAYER_TABLE_NAME = stringPreferencesKey("KEY_PLAYER_TABLE_NAME")
        val KEY_PLAYER_TABLE_ID = stringPreferencesKey("KEY_PLAYER_TABLE_ID")
    }

    private val Context.dataStore by preferencesDataStore(name = APP_DATA_STORE_NAME)

    suspend fun setPlayer(player: Player) {
        context.dataStore.edit { preferences ->
            preferences[KEY_PLAYER_UUID] = player.uuid
            preferences[KEY_PLAYER_NICK_NAME] = player.nickName
            preferences[KEY_PLAYER_TABLE_ID] = player.tableId
            preferences[KEY_PLAYER_TABLE_NAME] = player.tableName
        }
    }

    fun watchPlayer(): Flow<Player> {
        return context.dataStore.data.map { preferences ->
            Player(
                uuid = preferences[KEY_PLAYER_UUID] ?: "",
                nickName = preferences[KEY_PLAYER_NICK_NAME] ?: "",
                tableId = preferences[KEY_PLAYER_TABLE_ID] ?: "",
                tableName = preferences[KEY_PLAYER_TABLE_NAME] ?: ""
            )
        }
    }

    suspend fun deletePlayer() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_PLAYER_UUID)
            preferences.remove(KEY_PLAYER_NICK_NAME)
            preferences.remove(KEY_PLAYER_TABLE_ID)
            preferences.remove(KEY_PLAYER_TABLE_NAME)
        }
    }

    fun watchExampleBoolean(): Flow<Boolean> = watchDataStoreValue(Keys.EXAMPLE_BOOLEAN).map { value -> value ?: false }
    suspend fun getExampleBoolean(): Boolean = getDataStoreValue(Keys.EXAMPLE_BOOLEAN) ?: false
    suspend fun setExampleBoolean(value: Boolean) = setDataStoreValue(Keys.EXAMPLE_BOOLEAN, value)
    suspend fun removeExampleBoolean() = removeDataStoreValue(Keys.EXAMPLE_BOOLEAN)

    fun watchExampleDouble(): Flow<Double> = watchDataStoreValue(Keys.EXAMPLE_DOUBLE).map { value -> value ?: 0.0 }
    suspend fun getExampleDouble(): Double = getDataStoreValue(Keys.EXAMPLE_DOUBLE) ?: 0.0
    suspend fun setExampleDouble(value: Double) = setDataStoreValue(Keys.EXAMPLE_DOUBLE, value)
    suspend fun removeExampleDouble() = removeDataStoreValue(Keys.EXAMPLE_DOUBLE)

    fun watchExampleStringSet(): Flow<Set<String>> = watchDataStoreValue(Keys.EXAMPLE_STRING_SET).map { value -> value ?: emptySet() }
    suspend fun getExampleStringSet(): Set<String> = getDataStoreValue(Keys.EXAMPLE_STRING_SET) ?: emptySet()
    suspend fun setExampleStringSet(value: Set<String>) = setDataStoreValue(Keys.EXAMPLE_STRING_SET, value)
    suspend fun removeExampleStringSet() = removeDataStoreValue(Keys.EXAMPLE_STRING_SET)

    private fun <T> watchDataStoreValue(key: Preferences.Key<T>): Flow<T?> = context.dataStore.data.map { preferences -> preferences[key] }
    private suspend fun <T> getDataStoreValue(key: Preferences.Key<T>): T? = context.dataStore.data.map { preferences -> preferences[key] }.firstOrNull()
    private suspend fun <T, M> getDataStoreValue(key: Preferences.Key<T>, map: (T?) -> M?): M? = context.dataStore.data.map { preferences -> map(preferences[key]) }.firstOrNull()
    private suspend fun <T> setDataStoreValue(key: Preferences.Key<T>, value: T) = context.dataStore.edit { preferences -> preferences[key] = value }
    private suspend fun removeDataStoreValue(key: Preferences.Key<*>) = context.dataStore.edit { preferences -> preferences.remove(key) }
}
