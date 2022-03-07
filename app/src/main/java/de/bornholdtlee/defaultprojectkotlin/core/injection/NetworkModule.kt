package de.bornholdtlee.defaultprojectkotlin.core.injection

import de.bornholdtlee.defaultprojectkotlin.BuildConfig
import de.bornholdtlee.defaultprojectkotlin.data.api.ApiInterface
import de.bornholdtlee.defaultprojectkotlin.data.api.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
}

@Suppress("MagicNumber")
private fun provideOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    }

//    builder.addInterceptor(AuthInterceptor())
    builder.readTimeout(10, TimeUnit.SECONDS)
    builder.connectTimeout(10, TimeUnit.SECONDS)
    builder.writeTimeout(10, TimeUnit.SECONDS)

    return builder.build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()
    .create(ApiInterface::class.java)
