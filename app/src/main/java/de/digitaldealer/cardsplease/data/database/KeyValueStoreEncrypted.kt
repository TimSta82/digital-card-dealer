package de.digitaldealer.cardsplease.data.database

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import de.digitaldealer.cardsplease.R

class KeyValueStoreEncrypted(context: Context) {

    private companion object {
        const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
        const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
    }

    private val masterKeyAlias = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val sharedPreferencesReader = EncryptedSharedPreferences.create(
        context,
        context.getString(R.string.app_name) + "_encrypted",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val sharedPreferencesWriter: SharedPreferences.Editor = sharedPreferencesReader.edit()

    var apiAuthToken: String
        get() = getString(KEY_AUTH_TOKEN, "") ?: ""
        set(value) = putString(KEY_AUTH_TOKEN, value)

    var apiRefreshToken: String
        get() = getString(KEY_REFRESH_TOKEN, "") ?: ""
        set(value) = putString(KEY_REFRESH_TOKEN, value)

    private fun putString(key: String, value: String) = sharedPreferencesWriter.putString(key, value).apply()

    fun remove(key: String) = sharedPreferencesWriter.remove(key).apply()
    fun removeAll() = sharedPreferencesWriter.clear().apply()

    private fun getString(key: String, defaultValue: String?): String? = sharedPreferencesReader.getString(key, defaultValue) ?: defaultValue
}
