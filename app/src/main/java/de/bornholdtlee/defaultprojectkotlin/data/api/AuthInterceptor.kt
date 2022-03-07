package de.bornholdtlee.defaultprojectkotlin.data.api

import de.bornholdtlee.defaultprojectkotlin.data.database.KeyValueStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthInterceptor : Interceptor, KoinComponent {

    private val appKeyValueStore by inject<KeyValueStore>()

    private companion object {
        const val AUTHORIZATION_KEY = "Authorization"
        const val BEARER_TOKEN_PREFIX = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        runBlocking {
            val accessToken: String = kotlin.runCatching { appKeyValueStore.getExampleStringSet().first().first().toString() }.getOrElse { "" }
            proceed(
                request()
                    .newBuilder()
                    .addHeader(AUTHORIZATION_KEY, "$BEARER_TOKEN_PREFIX $accessToken")
                    .build()
            )
        }
    }
}
